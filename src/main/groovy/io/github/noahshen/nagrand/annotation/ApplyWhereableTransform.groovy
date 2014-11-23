package io.github.noahshen.nagrand.annotation
/**
 * Created by noahshen on 14-11-18.
 */

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used only for testing
 */
@Retention (RetentionPolicy.SOURCE)
@Target ([ElementType.METHOD])
@GroovyASTTransformationClass (["io.github.noahshen.nagrand.transform.WhereableASTTransformation"])
public @interface ApplyWhereableTransform {
}