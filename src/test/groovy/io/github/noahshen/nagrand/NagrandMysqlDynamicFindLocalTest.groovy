package io.github.noahshen.nagrand

import groovy.sql.Sql
import io.github.noahshen.nagrand.builders.SQLDialect
import io.github.noahshen.nagrand.models.Person

import java.util.logging.Level

class NagrandMysqlDynamicFindLocalTest extends GroovyTestCase {

    Nagrand nagrand
    Sql sql

    void setUp() {
        sql = Sql.newInstance("jdbc:MySQL://localhost:3306/test", "user", "123", "com.mysql.jdbc.Driver")
        nagrand = new Nagrand(sql, SQLDialect.MYSQL)
        nagrand.enableQueryLogging(Level.INFO)
        nagrand.stormify(Person, true)
    }

    void tearDown() {
        sql.execute("drop table Person")
        sql.close()
    }

    void "test id property is created on model object and is set to null"() {
        def person = new Person(name: 'Spiderman', age: 30)
        person.save()

        assert person.id != null
    }

    // context : save
    void "test that find by with single result"() {
        new Person(name: 'Spiderman', age: 30).save()
        new Person(name: 'Batman', age: 31).save()
        new Person(name: 'Superman', age: 32).save()
        new Person(name: 'Ironman', age: 32).save()

        def persons = Person.findByName("Batman")
        assert persons[0]?.age == 31

    }

    // context : save
    void "test that find by multi result"() {
        new Person(name: 'Spiderman', age: 30).save()
        new Person(name: 'Batman', age: 31).save()
        new Person(name: 'Superman', age: 32).save()
        new Person(name: 'Ironman', age: 32).save()

        def persons = Person.findByAge(32)
        assert persons.size() == 2
        assert persons*.name == ["Superman", "Ironman"]

    }

    // context : save
    void "test that find first"() {
        new Person(name: 'Spiderman', age: 30).save()
        new Person(name: 'Batman', age: 31).save()
        new Person(name: 'Superman', age: 32).save()
        new Person(name: 'Ironman', age: 32).save()

        Person person = Person.findFirstByAge(32)
        assert person
        assert person.name == "Superman"

        Person person2 = Person.findFirstByAge(100)
        assert !person2

    }

    // context : save
    void "test that find by multi condition"() {
        new Person(name: 'Spiderman', age: 30).save()
        new Person(name: 'Batman', age: 31).save()
        new Person(name: 'Superman', age: 32).save()
        new Person(name: 'Ironman', age: 32).save()

        List<Person> persons = Person.findByNameAndAge('Superman', 32)
        assert persons.size() == 1
        assert persons*.name == ["Superman"]

    }

    void "test that find by closure"() {
        new Person(name: 'Spiderman', age: 30).save()
        new Person(name: 'Batman', age: 31).save()
        new Person(name: 'Superman', age: 32).save()
        new Person(name: 'Ironman', age: 32).save()

        List<Person> persons = Person.findByAge(32) {
            order("name", "desc")
        }
        assert persons.size() == 2
        assert persons[0].name == "Superman"

    }

    void "test that find by closure limit"() {
        new Person(name: 'Spiderman', age: 30).save()
        new Person(name: 'Batman', age: 31).save()
        new Person(name: 'Superman', age: 32).save()
        new Person(name: 'Ironman', age: 32).save()

        List<Person> persons = Person.findByAge(32) {
            order("name", "desc")
            max(1)
        }
        assert persons.size() == 1
        assert persons[0].name == "Superman"

    }
    void "test that find by closure limit offset"() {
        new Person(name: 'Spiderman', age: 30).save()
        new Person(name: 'Batman', age: 31).save()
        new Person(name: 'Superman', age: 32).save()
        new Person(name: 'Ironman', age: 32).save()

        List<Person> persons = Person.findByAge(32) {
            order("name", "desc")
            offset(1)
        }
        assert persons.size() == 1
        assert persons[0].name == "Ironman"

    }
}
