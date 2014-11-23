package io.github.noahshen.nagrand.metadata
import io.github.noahshen.nagrand.annotation.Column

import java.lang.annotation.Annotation
import java.lang.reflect.Field

class FieldMetaData {
    Class clazz
    String name
    String columnName

    FieldMetaData(Class clazz, String name, String columnName) {
        this.clazz = clazz
        this.name = name
        this.columnName = columnName
    }

    FieldMetaData(Field field) {
        this.clazz = field.type
        this.name = field.name
        Annotation columnAnno = field.getAnnotation(Column)
        this.columnName = columnAnno ? columnAnno.name() : field.name
    }
}
