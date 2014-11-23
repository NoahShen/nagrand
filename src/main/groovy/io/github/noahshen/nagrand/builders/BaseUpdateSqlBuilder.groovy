package io.github.noahshen.nagrand.builders

import io.github.noahshen.nagrand.metadata.ClassMetaData

abstract class BaseUpdateSqlBuilder extends BaseWhereableSqlBuilder {

    def entity

    BaseUpdateSqlBuilder(ClassMetaData classMetaData, entity) {
        super(classMetaData)
        this.entity = entity
    }
}
