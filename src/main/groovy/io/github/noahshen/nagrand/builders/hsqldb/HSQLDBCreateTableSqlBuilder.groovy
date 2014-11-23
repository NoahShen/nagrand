package io.github.noahshen.nagrand.builders.hsqldb
import io.github.noahshen.nagrand.builders.BaseCreateTableSqlBuilder
import io.github.noahshen.nagrand.builders.BuildResult
import io.github.noahshen.nagrand.metadata.ClassMetaData

class HSQLDBCreateTableSqlBuilder extends BaseCreateTableSqlBuilder {

    HSQLDBCreateTableSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }


    @Override
    BuildResult buildSqlAndValues() {
        def tableName = classMetaData.tableName
        def columnDefs = classMetaData.fields.collect { field -> "${field.columnName} ${HSQLDBTypeMapper.instance.getSqlType(field.clazz)}" }

        columnDefs.add(0, "${classMetaData.idField.columnName} ${HSQLDBTypeMapper.instance.getSqlType(classMetaData.idField.clazz)} GENERATED ALWAYS AS IDENTITY PRIMARY KEY")
        def sql = "CREATE TABLE IF NOT EXISTS ${tableName} (${columnDefs.join(', ')})"
        new BuildResult(sql: sql, values: null)
    }
}