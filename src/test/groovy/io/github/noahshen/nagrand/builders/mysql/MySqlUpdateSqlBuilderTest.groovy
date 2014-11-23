package io.github.noahshen.nagrand.builders.mysql

import io.github.noahshen.nagrand.annotation.Column
import io.github.noahshen.nagrand.annotation.Id
import io.github.noahshen.nagrand.metadata.ClassMetaData

class MySqlUpdateSqlBuilderTest extends GroovyTestCase {

    class Person {
        @Id
        @Column(name = "PersonID")
        Integer id

        @Column(name = "PersonName")
        def name

        @Column(name = "PersonAge")
        int age
    }

    MySqlUpdateSqlBuilder builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new MySqlUpdateSqlBuilder(classMetaData, new Person(name: "Noah", age: 17))
    }

    void "test if builder is created" () {
        assertNotNull builder
    }

    void "test the generated update query with where clause" () {
        builder.eq("id", 8787)
        def result = builder.buildSqlAndValues()

        assert result.sql == "UPDATE `Person` SET `PersonName` = ?, `PersonAge` = ? WHERE `PersonID` = ?"
        assert result.values.size() == 3
        assert result.values[0] == "Noah"
        assert result.values[1] == 17
        assert result.values[2] == 8787
    }

    void "test no where clause" () {
        def result = builder.buildSqlAndValues()

        assert result.sql == "UPDATE `Person` SET `PersonName` = ?, `PersonAge` = ?"
        assert result.values.size() == 2
        assert result.values[0] == "Noah"
        assert result.values[1] == 17
    }

}

