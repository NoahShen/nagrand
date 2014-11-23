package io.github.noahshen.nagrand.builders.mysql

import groovy.sql.Sql
import io.github.noahshen.nagrand.Nagrand
import io.github.noahshen.nagrand.builders.SQLDialect
import io.github.noahshen.nagrand.models.Item

import java.util.logging.Level

/**
 * Created by noahshen on 14-11-14.
 */
class MySqlTypeMapperLocalTest extends GroovyTestCase {
    Nagrand nagrand
    Sql sql


    void setUp() {
        MySqlTypeMapper.instance.reset()

        MySqlTypeMapper.instance.setDefaultType("VARCHAR(64)")
        MySqlTypeMapper.instance.setType((java.lang.String), "VARCHAR(16)")
        sql = Sql.newInstance("jdbc:MySQL://localhost:3306/test", "user", "123", "com.mysql.jdbc.Driver")

        nagrand = new Nagrand(sql, SQLDialect.MYSQL)
        nagrand.enableQueryLogging(Level.INFO)

        nagrand.stormify(Item, true)

    }

    void tearDown() {
        sql.execute("DROP TABLE Item")
        sql.close()

        MySqlTypeMapper.instance.reset()
    }

    void "test should customize the types"() {
        println MySqlTypeMapper.instance.defaultType
        def mysqlMappings = sql
                .rows("show columns from Item")
                .collectEntries { [it.field, it.type] }

        assert mysqlMappings["name"] == "varchar(16)"
        assert mysqlMappings["description"] == "varchar(64)"
    }
}
