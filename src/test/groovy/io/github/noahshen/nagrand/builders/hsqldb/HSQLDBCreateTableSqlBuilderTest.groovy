package io.github.noahshen.nagrand.builders.hsqldb

import io.github.noahshen.nagrand.metadata.ClassMetaData
/**
 * Created by noahshen on 14-10-19.
 */
class HSQLDBCreateTableSqlBuilderTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    HSQLDBCreateTableSqlBuilder builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person)
        builder = new HSQLDBCreateTableSqlBuilder(classMetaData)
    }

    void "test generate hsqldb create sql"() {
        def result = builder.buildSqlAndValues()
        assert result.sql == "CREATE TABLE IF NOT EXISTS Person (ID NUMERIC GENERATED ALWAYS AS IDENTITY PRIMARY KEY, name VARCHAR(255), age NUMERIC)"
    }
}
