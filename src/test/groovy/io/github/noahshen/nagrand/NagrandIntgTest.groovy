package io.github.noahshen.nagrand
import groovy.sql.Sql
import io.github.noahshen.nagrand.models.Person

import java.util.logging.Level

class NagrandIntgTest extends GroovyTestCase {

    Nagrand nagrand
    Sql sql

    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        nagrand = new Nagrand(sql)
        nagrand.enableQueryLogging(Level.INFO)
        nagrand.stormify(Person, true)
    }

    void tearDown() {
        sql.execute("drop table person if exists")
        sql.close()
    }

    // context : create table
    void "test that a table is created for enhanced class"() {
        assert sql.rows("select * from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'PERSON'").size() == 1
    }

    void "test table has columns defined in the class "() {
        def columns_names = sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = 'PERSON'").collect {
            it.column_name
        }

        ["NAME", "AGE"].each { assert columns_names.contains(it) }
    }

    // context : id, create table
    void "test that created table has id column"() {
        assert sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = 'PERSON'").collect {
            it.column_name
        }.contains("ID")
    }

    void "test id property is created on model object and is set to null"() {
        def person = new Person(name: 'Spiderman', age: 30)

        assert person.hasProperty("id")
        assert person.id == null
    }

    // context : save
    void "test that save inserts a model object to table"() {
        new Person(name: 'Spiderman', age: 30).save()

        sql.eachRow("select *  from person") { assert it.name == 'Spiderman' }
    }

    void "test that generated id is assigned to model after save"() {
        def person = new Person(name: 'Spiderman', age: 30).save()

        sql.eachRow("select *  from person") { assert it.id == person.id }
    }

    void "test if an object has id, it should be updated"() {
        def person = new Person(name: 'Spiderman', age: 30).save()

        person.name = 'Batman'
        person.save()
        def result = sql.rows("select *  from person")

        assert result.size() == 1
        assert result.first().name == 'Batman'
    }

    // context : delete
    void "test delete if object has id"() {
        def person = new Person(name: 'Spiderman', age: 30).save()
        person.delete()

        assert sql.rows("select count(*) as total_count from person").total_count == [0]
    }

    void "test should not delete if object is not saved"() {
        def person = new Person(name: 'Spiderman', age: 30)
        assert sql.rows("select count(*) as total_count from person").total_count == [0]
        person.delete()

        assert sql.rows("select count(*) as total_count from person").total_count == [0]
    }

    // context : where
    void "test where selects from table with where clause"() {
        new Person(name: 'Batman', age: 35).save()
        new Person(name: 'Spiderman', age: 30).save()

        assert Person.where {
            gt("age", 30)
        }.collect { it.name } == ["Batman"]
    }

    // context : get all
    void "test all lists all records in table"() {
        new Person(name: 'Batman', age: 35).save()
        new Person(name: 'Spiderman', age: 30).save()

        assert Person.all.collect { it.name } == sql.rows("select * from person").collect { it.name }
    }

    // context : find
    void "test get should find model by id"() {
        def batman = new Person(name: 'Batman', age: 35).save()
        def spiderman = new Person(name: 'Spiderman', age: 30).save()

        Person p = Person.get(batman.id)
        assert p.name == "Batman"
    }

    void "test find model by id when id doesnt exist"() {
        def batman = new Person(name: 'Batman', age: 35).save()
        def spiderman = new Person(name: 'Spiderman', age: 30).save()

        assert Person.get(123) == null // lets not complicate it by exceptions
    }

    //context : count
    void "test count"() {
        assert Person.count() == 0  // works as method

        def batman = new Person(name: 'Batman', age: 35).save()
        def spiderman = new Person(name: 'Spiderman', age: 30).save()

        assert Person.count() == 2    // as well as property
    }

    void "test count with where"() {
        assert Person.count() == 0  // works as method

        def batman = new Person(name: 'Batman', age: 35).save()
        def spiderman = new Person(name: 'Spiderman', age: 30).save()

        assert Person.count{
            gte("age", 30)
        } == 2
    }
}
