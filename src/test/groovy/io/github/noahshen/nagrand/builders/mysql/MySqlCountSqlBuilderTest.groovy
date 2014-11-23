package io.github.noahshen.nagrand.builders.mysql

import io.github.noahshen.nagrand.annotation.Column
import io.github.noahshen.nagrand.metadata.ClassMetaData

class MySqlCountSqlBuilderTest extends GroovyTestCase {

    class Person {
        @Column(name = "PersonID")
        Integer id

        @Column(name = "PersonName")
        def name

        @Column(name = "PersonAge")
        int age
    }

    MySqlCountSqlBuilder builder
    ClassMetaData classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new MySqlCountSqlBuilder(classMetaData)
    }


    void "test buildSqlAndValues"() {
        builder.eq("name", "Noah").gt("age", 10)
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT COUNT(1) as \"count\" FROM `Person` WHERE `PersonName` = ? AND `PersonAge` > ?"
        assert result.values.size() == 2
        assert result.values[0] == "Noah"
        assert result.values[1] == 10
    }

    void "test buildSqlAndValues no where"() {
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT COUNT(1) as \"count\" FROM `Person`"
        assert result.values.size() == 0
    }

}

