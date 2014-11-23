package io.github.noahshen.nagrand.builders.mysql

import io.github.noahshen.nagrand.builders.BaseSelectSqlBuilder
import io.github.noahshen.nagrand.builders.BuildResult
import io.github.noahshen.nagrand.metadata.ClassMetaData

class MySqlSelectSqlBuilder extends BaseSelectSqlBuilder {

    MySqlSelectSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    @Override
    BuildResult buildSqlAndValues() {
        def fields = classMetaData.getAllFields()
        def projections = fields.collect { "`${it.columnName}` as \"${it.name}\"" }.join(", ")

        def values = []
        def where = MySqlStatementBuilders.generateQuerySql(queryCondition, classMetaData, values)
        def sql = "SELECT ${projections} FROM `${classMetaData.tableName}`${where}"
        new BuildResult(sql: sql, values: values)
    }

}
