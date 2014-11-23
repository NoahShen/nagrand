package io.github.noahshen.nagrand.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Created by noahshen on 14-10-11.
 */
@Retention (RetentionPolicy.RUNTIME)
@Target ([ElementType.FIELD])
public @interface AutoDateCreated {

    boolean autoTimestamp() default true

}