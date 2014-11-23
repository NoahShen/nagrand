package io.github.noahshen.nagrand.builders.mysql

import io.github.noahshen.nagrand.builders.BaseDeleteSqlBuilder
import io.github.noahshen.nagrand.builders.BuildResult
import io.github.noahshen.nagrand.metadata.ClassMetaData

class MySqlDeleteSqlBuilder extends BaseDeleteSqlBuilder {

    MySqlDeleteSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    @Override
    BuildResult buildSqlAndValues() {
        def values = []
        def where = MySqlStatementBuilders.generateQuerySql(queryCondition, classMetaData, values)
        def sql = "DELETE FROM `${classMetaData.tableName}`${where}"
        new BuildResult(sql: sql, values: values)
    }
}
