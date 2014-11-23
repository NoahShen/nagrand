package io.github.noahshen.nagrand.builders

import io.github.noahshen.nagrand.metadata.ClassMetaData

abstract class BaseInsertSqlBuilder extends BaseSqlBuilder {

    def entity

    BaseInsertSqlBuilder(ClassMetaData classMetaData, entity) {
        super(classMetaData)
        this.entity = entity
    }
}
