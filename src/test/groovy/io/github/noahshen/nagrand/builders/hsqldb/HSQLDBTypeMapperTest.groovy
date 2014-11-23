package io.github.noahshen.nagrand.builders.hsqldb
import groovy.sql.Sql
import io.github.noahshen.nagrand.Nagrand
import io.github.noahshen.nagrand.models.Item

import java.util.logging.Level
/**
 * Created by noahshen on 14-11-14.
 */
class HSQLDBTypeMapperTest extends GroovyTestCase {
    Nagrand nagrand
    Sql sql


    void setUp() {
        HSQLDBTypeMapper.instance.reset()

        HSQLDBTypeMapper.instance.setDefaultType("VARCHAR(64)")
        HSQLDBTypeMapper.instance.setType((java.lang.String), "VARCHAR(16)")

        sql = Sql.newInstance("jdbc:hsqldb:mem:randomdb;shutdown=true", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        nagrand = new Nagrand(sql)
        nagrand.enableQueryLogging(Level.INFO)

        nagrand.stormify(Item, true)

    }

    void tearDown() {
        sql.execute("DROP TABLE ITEM IF EXISTS")
        sql.close()

        HSQLDBTypeMapper.instance.reset()
    }

    void "test should customize the types"() {
        println HSQLDBTypeMapper.instance.defaultType
        def mysqlMappings = sql
                .rows("select COLUMN_NAME, DTD_IDENTIFIER from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = 'ITEM'")
                .collectEntries { [it.column_name, it.dtd_identifier] }

        assert mysqlMappings["NAME"] == "VARCHAR(16)"
        assert mysqlMappings["DESCRIPTION"] == "VARCHAR(64)"
    }
}
