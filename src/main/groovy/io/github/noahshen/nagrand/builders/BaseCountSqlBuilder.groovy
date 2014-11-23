package io.github.noahshen.nagrand.builders

import io.github.noahshen.nagrand.metadata.ClassMetaData

abstract class BaseCountSqlBuilder extends BaseWhereableSqlBuilder {

    BaseCountSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
