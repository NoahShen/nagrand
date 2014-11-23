package io.github.noahshen.nagrand.builders.mysql

import io.github.noahshen.nagrand.metadata.ClassMetaData

class MySqlDeleteSqlBuilderTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    MySqlDeleteSqlBuilder builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new MySqlDeleteSqlBuilder(classMetaData)
    }

    void "test if builder is created" () {
        assertNotNull builder
    }

    void "test the generated delete query" () {
        def result = builder.buildSqlAndValues()
        assert result.sql == "DELETE FROM `Person`"
        assert !result.values
    }

    void "test the generated delete query with where clause" () {
        def result = builder.eq("name", "Noah").buildSqlAndValues()

        assert result.sql == "DELETE FROM `Person` WHERE `name` = ?"
        assert result.values.size() == 1
        assert result.values[0] == "Noah"
    }

    void "test no where clause" () {
        def result = builder.buildSqlAndValues()

        assert result.sql == "DELETE FROM `Person`"
        assert !result.values
    }

}

