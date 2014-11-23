package io.github.noahshen.nagrand
import groovy.sql.Sql
import io.github.noahshen.nagrand.models.ClassWithEvents

import java.util.logging.Level

class NagrandEventTest extends GroovyTestCase {

    Nagrand nagrand
    Sql sql

    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        nagrand = new Nagrand(sql)
        nagrand.enableQueryLogging(Level.INFO)
        nagrand.stormify(ClassWithEvents, true)
    }

    void tearDown() {
        sql.execute("drop table ClassWithEvents if exists")
        sql.close()
    }


    // context : save
    void "test beforeInsert and afterInsert"() {
        def entity = new ClassWithEvents(name: 'Spiderman').save()
        assert entity.beforeInsertProperty
        assert entity.afterInsertProperty
    }

    // context : save
    void "test beforeUpdate and afterUpdate"() {
        def entity = new ClassWithEvents(name: 'Spiderman').save()
        entity.name = "Superman"
        entity.save()
        assert entity.beforeUpdateProperty
        assert entity.afterUpdateProperty
    }

    // context : save
    void "test beforeDelete and afterDelete"() {
        def entity = new ClassWithEvents(name: 'Spiderman').save()
        entity.delete()
        assert entity.beforeDeleteProperty
        assert entity.afterDeleteProperty
    }

}
