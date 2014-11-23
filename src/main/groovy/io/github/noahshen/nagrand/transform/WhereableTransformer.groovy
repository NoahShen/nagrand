package io.github.noahshen.nagrand.transform

import io.github.noahshen.nagrand.annotation.Entity
import org.codehaus.groovy.ast.ClassCodeVisitorSupport
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.SourceUnit
/**
 * Created by noahshen on 14-11-18.
 */
class WhereableTransformer extends ClassCodeVisitorSupport {

    static final Set WHEREABLE_METHODS = [
            "find",
            "where",
            "findWhere",
            "findFirst",
            "findFirstWhere",
            "findAll",
            "count",
            "getCount"
    ]

    static final Set BASIC_OPERATOR = ["eq", "nq", "gt", "lt", "gte", "lte"]

    static final Map WHEREABLE_OPERATOR = [
            "==": "eq",
            "!=": "nq",
            ">" : "gt",
            "<" : "lt",
            ">=": "gte",
            "<=": "lte",
            "in": "inList"
    ]

    static final Map PROPERTY_WHEREABLE_OPERATOR = [
            "eq": "eqProperty",
            "nq": "neProperty",
            "gt" : "gtProperty",
            "lt" : "ltProperty",
            "gte": "geProperty",
            "lte": "leProperty"
    ]

    static final Map MULTI_CONDITION_OPERATOR_MAP = [
            "&&": "and",
            "||": "or"
    ]

    SourceUnit sourceUnit

    WhereableTransformer(SourceUnit sourceUnit) {
        this.sourceUnit = sourceUnit
    }

    @Override
    protected SourceUnit getSourceUnit() {
        return this.sourceUnit
    }


    @Override
    void visitMethodCallExpression(MethodCallExpression call) {
        Expression objectExpression = call.objectExpression
        Expression methodExpression = call.method
        Expression argumentsExpression = call.arguments
        ClassNode entityClassNode = extractEntityClassNode(objectExpression)
        if (!entityClassNode) {
            super.visitMethodCallExpression(call)
            return
        }
        println "Entity ${entityClassNode}"
        if (!isWhereableMethod(methodExpression, WHEREABLE_METHODS)) {
            super.visitMethodCallExpression(call)
            return
        }
        ClosureExpression whereClosure = extractLastClosureArg(argumentsExpression)
        if (!whereClosure) {
            super.visitMethodCallExpression(call)
            return
        }
        List<String> propertyNames = getPropertyNames(entityClassNode)
        println "Entity propertyNames: ${propertyNames}"
        transformClosureExpression(propertyNames, whereClosure)
        super.visitMethodCallExpression(call)
    }

    private void transformClosureExpression(List<String> propertyNames, ClosureExpression closureExpression) {
        Statement oldStatement = closureExpression.code
        BlockStatement newStatement = new BlockStatement()
        if (oldStatement instanceof BlockStatement) {
            BlockStatement oldBockStatement = (BlockStatement) oldStatement
            addBlockStatementToNewQuery(oldBockStatement, newStatement, propertyNames)
            newStatement.variableScope = oldBockStatement.variableScope
            if (newStatement.statements) {
                closureExpression.code = newStatement
            }
        }
    }

    private void addBlockStatementToNewQuery(BlockStatement oldStatement, BlockStatement newStatement, List<String> propertyNames) {
        oldStatement.statements?.each { statement ->
            if (statement instanceof ExpressionStatement) {
                if (statement.expression instanceof BinaryExpression) {
                    BinaryExpression binaryExpression = statement.expression
                    addNewStatement(binaryExpression, newStatement, propertyNames)
                } else {
                    newStatement.addStatement(statement)
                }
            }
        }
    }

    private void addNewStatement(Expression expression,
                                 BlockStatement newStatement,
                                 List<String> propertyNames) {
        if (!(expression instanceof BinaryExpression)) {
            super.addError("unknown groovy-style whereable", expression)
            return
        }

        println "transform expression ${expression.text}"

        if (MULTI_CONDITION_OPERATOR_MAP.containsKey(expression.operation.text)) {
            addMultiConditionStatement(expression, newStatement, propertyNames)
        } else {
            addConditionStatement(expression, newStatement, propertyNames)
        }
    }

    private void addConditionStatement(BinaryExpression expression, BlockStatement newStatement, List<String> propertyNames) {
        String method = WHEREABLE_OPERATOR[expression.operation.text]
        if (!method) {
            super.addError("unknown groovy-style whereable", expression)
            return
        }
        switch (method) {
            case BASIC_OPERATOR:
                addBasicCondition(method, expression, newStatement, propertyNames)
                break
            case "inList":
                addInListCondition(expression, newStatement, propertyNames)
                break
            default:
                super.addError("unknown groovy-style whereable", expression)
                return
        }
    }

    private void addInListCondition(BinaryExpression expression, BlockStatement newStatement, List<String> propertyNames) {
        Expression left = expression.leftExpression
        if (!(left instanceof VariableExpression) || !propertyNames.contains(left.name)) {
            super.addError("Invalid entity property for groovy-style whereable", left)
            return
        }
        Expression right = expression.rightExpression
        if (right instanceof RangeExpression) {
            def newExpr = new ExpressionStatement(
                    new MethodCallExpression(
                            new VariableExpression("this"),
                            new ConstantExpression("inList"),
                            new ArgumentListExpression(
                                    new ConstantExpression(left.name),
                                    new RangeExpression(right.from, right.to, right.inclusive)
                            )
                    )
            )
            newStatement.addStatement(newExpr)
        } else if (right instanceof VariableExpression){
            def newExpr = new ExpressionStatement(
                    new MethodCallExpression(
                            new VariableExpression("this"),
                            new ConstantExpression("inList"),
                            new ArgumentListExpression(
                                    new ConstantExpression(left.name),
                                    new VariableExpression(right.name)
                            )
                    )
            )
            newStatement.addStatement(newExpr)
        } else {
            super.addError("Unknown expression", right)
        }
    }

    private void addBasicCondition(String method, BinaryExpression expression, BlockStatement newStatement, List<String> propertyNames) {
        Expression left = expression.leftExpression
        if (!(left instanceof VariableExpression) || !propertyNames.contains(left.name)) {
            super.addError("Invalid entity property for groovy-style whereable", left)
            return
        }

        Expression right = expression.rightExpression
        if (right instanceof ConstantExpression) {
            def newExpr = new ExpressionStatement(
                    new MethodCallExpression(
                            new VariableExpression("this"),
                            new ConstantExpression(method),
                            new ArgumentListExpression(
                                    new ConstantExpression(left.name),
                                    new ConstantExpression(right.value)
                            )
                    )
            )
            newStatement.addStatement(newExpr)
        } else if (right instanceof VariableExpression) {
            ExpressionStatement newExpr = null
            if (propertyNames.contains(right.name)) {
                newExpr = new ExpressionStatement(
                        new MethodCallExpression(
                                new VariableExpression("this"),
                                new ConstantExpression(PROPERTY_WHEREABLE_OPERATOR[method]),
                                new ArgumentListExpression(
                                        new ConstantExpression(left.name),
                                        new ConstantExpression(right.name)
                                )
                        )
                )
            } else {
                newExpr = new ExpressionStatement(
                        new MethodCallExpression(
                                new VariableExpression("this"),
                                new ConstantExpression(method),
                                new ArgumentListExpression(
                                        new ConstantExpression(left.name),
                                        new VariableExpression(right.name)
                                )
                        )
                )
            }
            newStatement.addStatement(newExpr)
        } else {
            super.addError("Unknown expression", expression)
        }
    }

    private void addMultiConditionStatement(BinaryExpression binaryExpression,
                                            BlockStatement newStatement,
                                            List<String> propertyNames) {
        String conditionMethod = MULTI_CONDITION_OPERATOR_MAP[binaryExpression.operation.text]
        BlockStatement subConditionStatement = new BlockStatement()
        addNewStatement(binaryExpression.leftExpression, subConditionStatement, propertyNames)
        addNewStatement(binaryExpression.rightExpression, subConditionStatement, propertyNames)
        def newExpr = new ExpressionStatement(
                new MethodCallExpression(
                        new VariableExpression("this"),
                        new ConstantExpression(conditionMethod),
                        new ArgumentListExpression(
                                new ClosureExpression(null, subConditionStatement)
                        )
                )
        )
        newStatement.addStatement(newExpr)
    }

    private List<String> getPropertyNames(ClassNode entityClassNode) {
        def fields = entityClassNode.fields
        if (fields) {
            return fields.collect {
                it.name
            }
        }
        Class clazz = Class.forName(entityClassNode.name)
        clazz.declaredFields.findAll { !it.synthetic }.collect {
            it.name
        }
    }

    private ClosureExpression extractLastClosureArg(Expression expression) {
        if (expression instanceof ArgumentListExpression) {
            ArgumentListExpression ale = (ArgumentListExpression) expression
            def expressions = ale.expressions
            if (expressions) {
                Expression lastArgument = expressions[-1]
                if (lastArgument instanceof ClosureExpression) {
                    return lastArgument
                } else if (expression instanceof VariableExpression) {
                    // TODO closure var
                }
            }
        }
    }

    private Boolean isWhereableMethod(Expression expression, Set whereMethods) {
        if (expression instanceof ConstantExpression) {
            ConstantExpression constantExpression = expression
            String methodName = constantExpression.value
            return whereMethods.contains(methodName)
        }
        false
    }

    private ClassNode extractEntityClassNode(Expression objectExpression) {
        ClassNode classNode = null
        if (objectExpression instanceof ClassExpression) {
            ClassExpression classExpression = objectExpression
            classNode = classExpression.getType()
        }
        if (objectExpression instanceof VariableExpression) {
            VariableExpression variableExpression = objectExpression
            String simpleName = variableExpression.name
            classNode = extractClassNodeFromImport(simpleName)

        }
        if (!classNode) {
            return null
        }
        def annotations = classNode.annotations
        if (annotations) {
            def isEntity = annotations.any {
                String className = it.classNode.getName()
                Entity.name == className
            }
            if (isEntity) {
                return classNode
            }
        }
        String className = classNode.name
        Class clazz = Class.forName(className)
        Entity e = clazz.getAnnotation(Entity)
        if (e) {
            return classNode
        }
        return null
    }

    private ClassNode extractClassNodeFromImport(String simpleName) {
        def importNodes = sourceUnit.getAST().imports
        def entityImportNode = importNodes.find {
            it.alias == simpleName
        }
        if (entityImportNode) {
            return entityImportNode.type
        }
    }
}
