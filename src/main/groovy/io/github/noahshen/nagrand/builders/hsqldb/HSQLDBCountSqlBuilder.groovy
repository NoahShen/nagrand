package io.github.noahshen.nagrand.builders.hsqldb
import io.github.noahshen.nagrand.builders.BaseCountSqlBuilder
import io.github.noahshen.nagrand.builders.BuildResult
import io.github.noahshen.nagrand.metadata.ClassMetaData

class HSQLDBCountSqlBuilder extends BaseCountSqlBuilder {

    HSQLDBCountSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    @Override
    BuildResult buildSqlAndValues() {
        def values = []
        def where = HSQLDBStatementBuilders.generateQuerySql(queryCondition, classMetaData, values)
        def sql = "SELECT COUNT(1) as \"count\" FROM ${classMetaData.tableName}${where}"
        new BuildResult(sql: sql, values: values)
    }
}
