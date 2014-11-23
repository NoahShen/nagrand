package io.github.noahshen.nagrand.builders.hsqldb
import io.github.noahshen.nagrand.annotation.Column
import io.github.noahshen.nagrand.annotation.Id
import io.github.noahshen.nagrand.metadata.ClassMetaData

class HSQLDBSelectSqlBuilderTest extends GroovyTestCase {

    class Person {
        @Id
        @Column(name = "PersonID")
        Integer id

        @Column(name = "PersonName")
        def name

        @Column(name = "PersonAge")
        int age
    }

    HSQLDBSelectSqlBuilder builder
    ClassMetaData classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person.class)
        builder = new HSQLDBSelectSqlBuilder(classMetaData)
    }

    void "test if builder is created"() {
        assertNotNull builder
    }

    void "test buildSqlAndValues"() {
        builder.eq("name", "Noah").gt("age", 10)
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT PersonID as \"id\", PersonName as \"name\", PersonAge as \"age\" FROM Person WHERE PersonName = ? AND PersonAge > ?"
        assert result.values.size() == 2
        assert result.values[0] == "Noah"
        assert result.values[1] == 10
    }

    void "test buildSqlAndValues no where"() {
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT PersonID as \"id\", PersonName as \"name\", PersonAge as \"age\" FROM Person"
        assert !result.values
    }


    void "test select by id"() {
        builder.idEq(123)
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT PersonID as \"id\", PersonName as \"name\", PersonAge as \"age\" FROM Person WHERE PersonID = ?"
        assert result.values.size() == 1
        assert result.values[0] == 123
    }


    void "test buildSqlAndValues Order by"() {
        builder.eq("name", "Noah").gt("age", 10).order("age", "desc")
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT PersonID as \"id\", PersonName as \"name\", PersonAge as \"age\" FROM Person WHERE PersonName = ? AND PersonAge > ? ORDER BY PersonAge DESC"
        assert result.values.size() == 2
        assert result.values[0] == "Noah"
        assert result.values[1] == 10
    }

    void "test buildSqlAndValues Limit"() {
        builder.eq("name", "Noah").gt("age", 10).max(10)
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT PersonID as \"id\", PersonName as \"name\", PersonAge as \"age\" FROM Person WHERE PersonName = ? AND PersonAge > ? LIMIT 10 OFFSET 0"
        assert result.values.size() == 2
        assert result.values[0] == "Noah"
        assert result.values[1] == 10
    }

    void "test buildSqlAndValues Limit Offset"() {
        builder.eq("name", "Noah").gt("age", 10).offset(7)
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT PersonID as \"id\", PersonName as \"name\", PersonAge as \"age\" FROM Person WHERE PersonName = ? AND PersonAge > ? LIMIT 0 OFFSET 7"
        assert result.values.size() == 2
        assert result.values[0] == "Noah"
        assert result.values[1] == 10
    }

    void "test buildSqlAndValues Order by limit no where"() {
        builder.order("age", "ASC").order("name").offset(7).max(17)
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT PersonID as \"id\", PersonName as \"name\", PersonAge as \"age\" FROM Person ORDER BY PersonAge ASC, PersonName DESC LIMIT 17 OFFSET 7"
        assert result.values.size() == 0
    }

    void "test buildSqlAndValues Order by limit"() {
        builder.eq("name", "Noah").gt("age", 10).order("age", "ASC").order("name").offset(7).max(17)
        def result = builder.buildSqlAndValues()
        assert result.sql == "SELECT PersonID as \"id\", PersonName as \"name\", PersonAge as \"age\" FROM Person WHERE PersonName = ? AND PersonAge > ? ORDER BY PersonAge ASC, PersonName DESC LIMIT 17 OFFSET 7"
        assert result.values.size() == 2
        assert result.values[0] == "Noah"
        assert result.values[1] == 10
    }

}

