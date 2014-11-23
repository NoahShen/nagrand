package io.github.noahshen.nagrand.builders

import io.github.noahshen.nagrand.builders.hsqldb.*
import io.github.noahshen.nagrand.builders.mysql.*
import io.github.noahshen.nagrand.metadata.ClassMetaData

/**
 * Created by noahshen on 14-10-19.
 */
class SQLBuilderFactoryTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    ClassMetaData classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person)
    }

    void "test create instance"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        assert factory

        def factory2 = SQLBuilderFactory.getInstance()
        assert factory.is(factory2)

    }

    void "test create CreateTableSqlBuilder"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        def builder = factory.createCreateTableSqlBuilder(SQLDialect.HSQLDB, classMetaData)
        assert builder instanceof HSQLDBCreateTableSqlBuilder

        def mysqlBuilder = factory.createCreateTableSqlBuilder(SQLDialect.MYSQL, classMetaData)
        assert mysqlBuilder instanceof MySqlCreateTableSqlBuilder
    }

    void "test create CountSqlBuilder"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        def builder = factory.createCountSqlBuilder(SQLDialect.HSQLDB, classMetaData)
        assert builder instanceof HSQLDBCountSqlBuilder

        def mysqlBuilder = factory.createCountSqlBuilder(SQLDialect.MYSQL, classMetaData)
        assert mysqlBuilder instanceof MySqlCountSqlBuilder
    }

    void "test create DeleteSqlBuilder"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        def builder = factory.createDeleteSqlBuilder(SQLDialect.HSQLDB, classMetaData)
        assert builder instanceof HSQLDBDeleteSqlBuilder

        def mysqlBuilder = factory.createDeleteSqlBuilder(SQLDialect.MYSQL, classMetaData)
        assert mysqlBuilder instanceof MySqlDeleteSqlBuilder
    }

    void "test create InsertSqlBuilder"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        def builder = factory.createInsertSqlBuilder(SQLDialect.HSQLDB, classMetaData, new Person())
        assert builder instanceof HSQLDBInsertSqlBuilder

        def mysqlBuilder = factory.createInsertSqlBuilder(SQLDialect.MYSQL, classMetaData, new Person())
        assert mysqlBuilder instanceof MySqlInsertSqlBuilder
    }

    void "test create SelectSqlBuilder"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        def builder = factory.createSelectSqlBuilder(SQLDialect.HSQLDB, classMetaData)
        assert builder instanceof HSQLDBSelectSqlBuilder

        def mysqlBuilder = factory.createSelectSqlBuilder(SQLDialect.MYSQL, classMetaData)
        assert mysqlBuilder instanceof MySqlSelectSqlBuilder
    }

    void "test create UpdateSqlBuilder"() {
        SQLBuilderFactory factory = SQLBuilderFactory.getInstance()
        def builder = factory.createUpdateSqlBuilder(SQLDialect.HSQLDB, classMetaData, new Person())
        assert builder instanceof HSQLDBUpdateSqlBuilder

        def mysqlBuilder = factory.createUpdateSqlBuilder(SQLDialect.MYSQL, classMetaData, new Person())
        assert mysqlBuilder instanceof MySqlUpdateSqlBuilder
    }
}
