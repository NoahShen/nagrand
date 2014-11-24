package io.github.noahshen.nagrand

import io.github.noahshen.nagrand.models.ClassWithNumbers
import io.github.noahshen.nagrand.models.Person
import org.junit.Test

import java.sql.Connection
import java.sql.DriverManager


class NagrandConstructionTest {

    //context : creation
    @Test
    void "should create nagrand with instance of groovy Sql"() {
        def sql = groovy.sql.Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        def g = new Nagrand(sql)
        g.register(Person, true)

        assert Person.count() == 0 // nagrand should work
        assert sql == g.sql
    }

    @Test
    void "should create nagrand with connection object"() {
        Class.forName("org.hsqldb.jdbcDriver");
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:database", "sa", "");
        def nagrand = new Nagrand(connection)
        nagrand.register(Person, true)

        assert Person.count() == 0
        assert nagrand.sql.connection == connection
    }

    @Test
    void "should create nagrand with memory db with no constructor arg"() {
        def nagrand = new Nagrand()
        nagrand.register(Person, true) // should create table

        assert Person.count() == 0 // nagrand should work
        assert "jdbc:hsqldb:mem:database" == nagrand.sql.connection.getMetaData().getURL()
    }

    @Test
    void "should create nagrand with file db with String constructor arg"() {
        def nagrand = new Nagrand("tmp/db/test-db")
        nagrand.register(Person, true) // should create table

        assert Person.count() == 0 // nagrand should work
        assert "jdbc:hsqldb:file:tmp/db/test-db" == nagrand.sql.connection.getMetaData().getURL()
        assert new File("tmp/db/test-db.properties").exists()
    }

    @Test
    void "should be able to chain stormify"() {
        def nagrand = new Nagrand().register(Person, true).register(ClassWithNumbers, true) // should create table

        assert nagrand instanceof Nagrand
        assert Person.count() == 0 // nagrand should work
        assert ClassWithNumbers.count() == 0 // nagrand should work
    }

}
