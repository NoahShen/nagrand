package io.github.noahshen.nagrand.builders.mysql

import io.github.noahshen.nagrand.annotation.Column
import io.github.noahshen.nagrand.annotation.Id
import io.github.noahshen.nagrand.metadata.ClassMetaData

class MySqlInsertSqlBuilderTest extends GroovyTestCase {
    class Person {
        @Id
        Integer id

        @Column(name = "PersonName")
        def name

        @Column(name = "PersonAge")
        int age
    }

    def builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new MySqlInsertSqlBuilder(classMetaData, new Person(name: "Noah", age: 17))
    }

    void "test if builder is created" () {
        assertNotNull builder
    }

    void "test the generated insert query" () {
        def result = builder.buildSqlAndValues()

        assert result.sql == "INSERT INTO `Person` (`PersonName`, `PersonAge`) VALUES (?, ?)"
        assert result.values.size() == 2
        assert result.values[0] == "Noah"
        assert result.values[1] == 17
    }

}

