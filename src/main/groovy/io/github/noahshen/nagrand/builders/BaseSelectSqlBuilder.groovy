package io.github.noahshen.nagrand.builders

import io.github.noahshen.nagrand.metadata.ClassMetaData

abstract class BaseSelectSqlBuilder extends BaseWhereableSqlBuilder {

    BaseSelectSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
