package io.github.noahshen.nagrand
import groovy.sql.Sql
import io.github.noahshen.nagrand.models.ClassAutoTimestamp
import io.github.noahshen.nagrand.models.ClassAutoTimestampByAnno
import io.github.noahshen.nagrand.models.ClassNoTimestamp
import org.junit.After
import org.junit.Before
import org.junit.Test

import java.util.logging.Level

/**
 * A bad test is better than no test :)
 */
class NagrandAutoTimestampTests {

    Nagrand nagrand
    Sql sql

    @Before
    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        nagrand = new Nagrand(sql)
        nagrand.enableQueryLogging(Level.INFO)
        nagrand.stormify(ClassAutoTimestamp, true)
        nagrand.stormify(ClassAutoTimestampByAnno, true)
        nagrand.stormify(ClassNoTimestamp, true)
    }

    @After
    void tearDown() {
        sql.execute("drop table ClassAutoTimestamp if exists")
        sql.execute("drop table ClassAutoTimestampByAnno if exists")
        sql.execute("drop table ClassNoTimestamp if exists")
        sql.close()
    }


    @Test
    void "should be able to save"() {
        new ClassAutoTimestamp(name: "zxsawe").save()

        def result = ClassAutoTimestamp.all()

        assert result.size() == 1
        ClassAutoTimestamp classAutoTimestamp = result.first()
        assert classAutoTimestamp.dateCreated
        assert classAutoTimestamp.lastUpdated
    }

    @Test
    void "should be able to get by annotated Id"() {
        def object = new ClassAutoTimestamp(name: "zxsawe").save()

        ClassAutoTimestamp entity = ClassAutoTimestamp.get(object.id)
        entity.name = "abc"
        Thread.sleep(1000)
        entity.save()

        ClassAutoTimestamp entity2 = ClassAutoTimestamp.get(entity.id)

        assert entity2.dateCreated != entity2.lastUpdated
    }

    @Test
    void "save time by annotation"() {
        new ClassAutoTimestampByAnno(name: "zxsawe").save()

        def result = sql.rows("select * from ${ClassAutoTimestampByAnno.simpleName.toUpperCase()}".toString())

        assert result.size() == 1
        assert result.first().addTime
        assert result.first().updateTime
    }

    @Test
    void "update lastDate by annotation"() {
        def object = new ClassAutoTimestampByAnno(name: "zxsawe").save()

        ClassAutoTimestampByAnno entity = ClassAutoTimestampByAnno.get(object.id)
        entity.name = "abc"
        Thread.sleep(1000)
        entity.save()

        ClassAutoTimestampByAnno entity2 = ClassAutoTimestampByAnno.get(entity.id)

        assert entity2.addTime != entity2.updateTime
    }

    @Test
    void "save no time"() {
        new ClassNoTimestamp(name: "zxsawe").save()

        def result = sql.rows("select * from ${ClassNoTimestamp.simpleName.toUpperCase()}".toString())

        assert result.size() == 1
        assert !result.first().addTime
        assert !result.first().updateTime
    }

    @Test
    void "update no time"() {
        def object = new ClassNoTimestamp(name: "zxsawe").save()

        ClassNoTimestamp entity = ClassNoTimestamp.get(object.id)
        entity.name = "abc"
        Thread.sleep(1000)
        entity.save()

        ClassAutoTimestampByAnno entity2 = ClassNoTimestamp.get(entity.id)

        assert !entity2.addTime
        assert !entity2.updateTime
    }


}
