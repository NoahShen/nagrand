package io.github.noahshen.nagrand.builders

import io.github.noahshen.nagrand.metadata.ClassMetaData

abstract class BaseSqlBuilder {

    ClassMetaData classMetaData

    BaseSqlBuilder(ClassMetaData classMetaData) {
        this.classMetaData = classMetaData
    }

    abstract BuildResult buildSqlAndValues()
}
