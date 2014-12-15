package io.github.noahshen.nagrand
import groovy.sql.Sql
import io.github.noahshen.nagrand.models.PersonWithIgnoreProperty
import org.junit.After
import org.junit.Before
import org.junit.Test

class NagrandIgnoreColumnTests {

    Nagrand nagrand
    Sql sql
    String tableName = PersonWithIgnoreProperty.simpleName.toUpperCase()

    @Before
    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        nagrand = new Nagrand(sql)
        nagrand.register(PersonWithIgnoreProperty, true)
    }

    @After
    void tearDown() {
        sql.execute("drop table PersonWithIgnoreProperty if exists")
        sql.close()
    }

    @Test
    void "test if Ignore column created"() {
        assert !sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ?", [tableName])
                .collect { it.column_name }
                .contains("MEMO")
    }


    @Test
    void "test ignore column"() {
        new PersonWithIgnoreProperty(name: "Noah", age:27, memo:"memo").save()

        def result = sql.rows("select * from ${tableName}".toString())

        assert result.size() == 1
        assert !result.first().hasProperty("memo")
    }


    @Test
    void "test ignore column 2"() {
        new PersonWithIgnoreProperty(name: "Noah", age:27, memo:"memo").save()

        def allPersons = PersonWithIgnoreProperty.getAll()

        assert allPersons.size() == 1
        PersonWithIgnoreProperty p = allPersons.first()
        assert !p.memo

    }


}
