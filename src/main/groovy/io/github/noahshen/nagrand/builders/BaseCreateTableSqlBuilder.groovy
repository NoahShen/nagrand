package io.github.noahshen.nagrand.builders

import io.github.noahshen.nagrand.metadata.ClassMetaData

abstract class BaseCreateTableSqlBuilder extends BaseSqlBuilder {

    BaseCreateTableSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}