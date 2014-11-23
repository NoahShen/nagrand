package io.github.noahshen.nagrand.transform
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.kohsuke.MetaInfServices

@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
@MetaInfServices
public class WhereableASTTransformation implements ASTTransformation {

    public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {

        def isTransform = System.getProperty("entity.transform")
        if (!isTransform) {
            return
        }

        WhereableTransformer whereableTransformer = new WhereableTransformer(sourceUnit)
        ModuleNode ast = sourceUnit.getAST();
        List<ClassNode> classes = ast.getClasses();
        for (ClassNode aClass : classes) {
            whereableTransformer.visitClass(aClass)
        }
    }

}