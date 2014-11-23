package io.github.noahshen.nagrand.builders.hsqldb
import io.github.noahshen.nagrand.builders.BaseDeleteSqlBuilder
import io.github.noahshen.nagrand.builders.BuildResult
import io.github.noahshen.nagrand.metadata.ClassMetaData

class HSQLDBDeleteSqlBuilder extends BaseDeleteSqlBuilder {

    HSQLDBDeleteSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    @Override
    BuildResult buildSqlAndValues() {
        def values = []
        def where = HSQLDBStatementBuilders.generateQuerySql(queryCondition, classMetaData, values)
        def sql = "DELETE FROM ${classMetaData.tableName}${where}"
        new BuildResult(sql: sql, values: values)
    }
}
