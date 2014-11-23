package io.github.noahshen.nagrand.builders.mysql
import io.github.noahshen.nagrand.builders.BaseCreateTableSqlBuilder
import io.github.noahshen.nagrand.builders.BuildResult
import io.github.noahshen.nagrand.metadata.ClassMetaData

class MySqlCreateTableSqlBuilder extends BaseCreateTableSqlBuilder {

    MySqlCreateTableSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

    @Override
    BuildResult buildSqlAndValues() {
        def tableName = classMetaData.tableName
        def columnDefs = classMetaData.fields.collect { field -> "`${field.columnName}` ${MySqlTypeMapper.instance.getSqlType(field.clazz)}" }

        columnDefs.add(0, "`${classMetaData.idField.columnName}` ${MySqlTypeMapper.instance.getSqlType(classMetaData.idField.clazz)} NOT NULL AUTO_INCREMENT")
        columnDefs.add("PRIMARY KEY (`${classMetaData.idField.columnName}`)")

        def sql = "CREATE TABLE IF NOT EXISTS `${tableName}` (${columnDefs.join(', ')})"
        new BuildResult(sql: sql, values: null)
    }
}