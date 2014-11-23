package io.github.noahshen.nagrand.builders.mysql

import io.github.noahshen.nagrand.builders.BaseUpdateSqlBuilder
import io.github.noahshen.nagrand.builders.BuildResult
import io.github.noahshen.nagrand.metadata.ClassMetaData

class MySqlUpdateSqlBuilder extends BaseUpdateSqlBuilder {

    MySqlUpdateSqlBuilder(ClassMetaData classMetaData, entity) {
        super(classMetaData, entity)
    }

    @Override
    BuildResult buildSqlAndValues() {

        def updateFieldPlaceHolder = []
        def values = []
        classMetaData.fields.each {
            updateFieldPlaceHolder << "`${it.columnName}` = ?"
            values << entity.getProperty(it.name)
        }
        def where = MySqlStatementBuilders.generateQuerySql(queryCondition, classMetaData, values)
        def sql = "UPDATE `${classMetaData.tableName}` SET ${updateFieldPlaceHolder.join(", ")}${where}"
        new BuildResult(sql: sql, values: values)
    }
}
