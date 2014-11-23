package io.github.noahshen.nagrand.builders

import io.github.noahshen.nagrand.metadata.ClassMetaData

abstract class BaseDeleteSqlBuilder extends BaseWhereableSqlBuilder {

    BaseDeleteSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }
}
