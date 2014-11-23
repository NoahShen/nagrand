package io.github.noahshen.nagrand.builders.hsqldb

import io.github.noahshen.nagrand.builders.query.Query
import io.github.noahshen.nagrand.builders.query.condition.*
import io.github.noahshen.nagrand.metadata.ClassMetaData

/**
 * Created by noahshen on 14-11-13.
 */
class HSQLDBStatementBuilders {


    static String generateQuerySql(Query query, ClassMetaData metaData, List values) {
        def sqlSegs = []
        if (query.conditions) {
            def where = query.conditions.collect {
                generateConditionSql(it, metaData, values)
            }.join(" AND ")
            sqlSegs << "WHERE ${where}"
        }
        if (query.orderBy) {
            def order = query.orderBy.collect {
                generateOrderBySql(it, metaData)
            }.join(", ")
            sqlSegs << "ORDER BY ${order}"
        }
        if (query.offset > 0 || query.max > 0) {
            sqlSegs << "LIMIT ${query.max <= 0 ? 0 : query.max} OFFSET ${query.offset}"
        }
        if (sqlSegs) {
            return " " + sqlSegs.join(" ")
        }
        ""
    }

    static String generateOrderBySql(Query.Order order, ClassMetaData metaData) {
        def columnName = metaData[order.property]?.columnName
        def direction = order.direction
        "${columnName} ${direction.name()}"
    }

    static String generateConditionSql(Condition c, ClassMetaData metaData, List values) {
        if (c instanceof AndCondition) {
            def subConditions = c.conditions.collect {
                generateConditionSql(it, metaData, values)
            }.join(" AND ")
            return "( ${subConditions} )"
        } else if (c instanceof OrCondition) {
            def subConditions = c.conditions.collect {
                generateConditionSql(it, metaData, values)
            }.join(" OR ")
            return "( ${subConditions} )"
        } else if (c instanceof PropertyNameCondition) {
            def columnName = metaData[c.propertyName]?.columnName
            if (c instanceof PropertyValueCondition) {
                if (c instanceof Equals) {
                    values << c.value
                    return "${columnName} = ?"
                } else if (c instanceof NotEquals) {
                    values << c.value
                    return "${columnName} <> ?"
                } else if (c instanceof GreaterThan) {
                    values << c.value
                    return "${columnName} > ?"
                } else if (c instanceof GreaterThanEquals) {
                    values << c.value
                    return "${columnName} >= ?"
                }  else if (c instanceof LessThan) {
                    values << c.value
                    return "${columnName} < ?"
                } else if (c instanceof LessThanEquals) {
                    values << c.value
                    return "${columnName} <= ?"
                } else if (c instanceof In) {
                    def placeHolder = c.value.collect {
                        values << it
                        "?"
                    }.join(", ")
                    return "${columnName} IN (${placeHolder})"
                }  else if (c instanceof NotIn) {
                    def placeHolder = c.value.collect {
                        values << it
                        "?"
                    }.join(", ")
                    return "${columnName} NOT IN (${placeHolder})"
                } else if (c instanceof Like) {
                    values << c.value
                    return "${columnName} LIKE ?"
                } else if (c instanceof Between) {
                    values << c.from
                    values << c.to
                    return "${columnName} BETWEEN ? AND ?"
                }
            } else if (c instanceof PropertyComparisonCondition) {
                def otherColumnName = metaData[c.otherProperty]?.columnName
                if (c instanceof EqualsProperty) {
                    return "${columnName} = ${otherColumnName}"
                } else if (c instanceof NotEqualsProperty) {
                    return "${columnName} <> ${otherColumnName}"
                } else if (c instanceof GreaterThanEqualsProperty) {
                    return "${columnName} >= ${otherColumnName}"
                } else if (c instanceof GreaterThanProperty) {
                    return "${columnName} > ${otherColumnName}"
                } else if (c instanceof LessThanEqualsProperty) {
                    return "${columnName} <= ${otherColumnName}"
                } else if (c instanceof LessThanProperty) {
                    return "${columnName} < ${otherColumnName}"
                }
            } else if (c instanceof IsEmpty) {
                return "${columnName} = ''"
            } else if (c instanceof IsNotEmpty) {
                return "${columnName} <> ''"
            } else if (c instanceof IsNotNull) {
                return "${columnName} IS NOT NULL"
            } else if (c instanceof IsNull) {
                return "${columnName} IS NULL"
            }
        }
    }
}
