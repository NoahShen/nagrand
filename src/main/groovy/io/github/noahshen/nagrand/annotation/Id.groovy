package io.github.noahshen.nagrand.annotation
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention (RetentionPolicy.RUNTIME)
@Target ([ElementType.FIELD, ElementType.LOCAL_VARIABLE])
@interface Id {

}