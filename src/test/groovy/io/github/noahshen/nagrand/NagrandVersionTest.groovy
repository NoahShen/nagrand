package io.github.noahshen.nagrand
import groovy.sql.Sql
import io.github.noahshen.nagrand.models.ClassWithVersion

import java.util.logging.Level

class NagrandVersionTest extends GroovyTestCase {

    Nagrand nagrand
    Sql sql

    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        nagrand = new Nagrand(sql)
        nagrand.enableQueryLogging(Level.INFO)
        nagrand.stormify(ClassWithVersion, true)
    }

    void tearDown() {
        sql.execute("drop table ClassWithVersion if exists")
        sql.close()
    }


    // context : save
    void "test init version"() {
        def entity = new ClassWithVersion(name: 'Spiderman').save()
        assert entity.version == 1
    }

    // context : save
    void "test updated version"() {
        def entity = new ClassWithVersion(name: 'Spiderman').save()
        assert entity.version == 1

        entity.name = "Superman"
        entity.save()
        assert entity.version == 2
    }

    // context : save
    void "test updated failed version"() {
        def entity = new ClassWithVersion(name: 'Spiderman').save()
        assert entity.version == 1

        ClassWithVersion entity2 = ClassWithVersion.get(entity.id)
        entity2.name = "Batman"
        entity2.save()
        assert entity2.version == 2

        entity.name = "Superman"
        entity.save()
        assert entity.version == 1
    }
}
