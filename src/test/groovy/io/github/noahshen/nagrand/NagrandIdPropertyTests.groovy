package io.github.noahshen.nagrand
import groovy.sql.Sql
import io.github.noahshen.nagrand.models.ClassWithIdProperty
import org.junit.After
import org.junit.Before
import org.junit.Test
/**
 * A bad test is better than no test :)
 */
class NagrandIdPropertyTests {

    Nagrand nagrand
    Sql sql
    String tableName = ClassWithIdProperty.simpleName.toUpperCase()

    @Before
    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        nagrand = new Nagrand(sql)
        nagrand.stormify(ClassWithIdProperty, true)
    }

    @After
    void tearDown() {
        sql.execute("drop table ClassWithIdProperty if exists")
        sql.close()
    }

    @Test
    void "Annotated Id field should be used to as Primary Key in create table"() {
        assert sql.rows("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = ?", [tableName])
                .collect { it.column_name }
                .contains("ID")
    }


    @Test
    void "should be able to save"() {
        new ClassWithIdProperty(name: "zxsawe").save()

        def result = sql.rows("select * from ${tableName}".toString())

        assert result.size() == 1
        assert result.first().name == 'zxsawe'
    }

    @Test
    void "should be able to get by annotated Id"() {
        def object = new ClassWithIdProperty(name: "zxsawe").save()

        def result = ClassWithIdProperty.get(object.id)

        assert result.name == 'zxsawe'
    }

    @Test
    void "should be able to delete by annotated Id"() {
        def object = new ClassWithIdProperty(name: "zxsawe").save()

        object.delete()

        assert sql.rows("select count(*) as total_count from ${tableName}".toString()).total_count == [0]
    }

    @Test
    void "should be able to update by annotated Id"() {
        def object = new ClassWithIdProperty(name: "zxsawe").save()

        object.name = "updated_name"
        object.save()

        def result = sql.rows("select * from ${tableName}".toString())

        assert result.size() == 1
        assert result.first().name == "updated_name"
    }

}
