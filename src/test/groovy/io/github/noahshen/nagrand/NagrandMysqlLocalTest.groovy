package io.github.noahshen.nagrand
import groovy.sql.Sql
import io.github.noahshen.nagrand.annotation.Column
import io.github.noahshen.nagrand.annotation.Id
import io.github.noahshen.nagrand.builders.SQLDialect
import org.junit.After
import org.junit.Before
import org.junit.Test

import java.util.logging.Level

class NagrandMysqlLocalTest {


    class Item {
        @Id
        @Column(name = "ItemId")
        Integer uid

        @Column(name = "ItemName")
        String name

        @Column(name = "ItemDesc")
        def description

        @Column(name = "AddTime")
        Date addedOn
    }

    Nagrand nagrand
    Sql sql

    @Before
    void setUp() {
        sql = Sql.newInstance("jdbc:MySQL://localhost:3306/test", "user", "123", "com.mysql.jdbc.Driver")
        nagrand = new Nagrand(sql, SQLDialect.MYSQL)
        nagrand.enableQueryLogging(Level.INFO)
        nagrand.stormify(Item, true)
    }

    @After
    void tearDown() {
        sql.execute("drop table Item")
        sql.close()
    }


    @Test
    void "should be able to get by Id"() {
        def item = new Item(name: 'Spiderman', description: "desc", addedOn: new Date()).save()

        def result = Item.get(item.uid)

        assert result.name == 'Spiderman'
    }


    @Test
    void "should be able to delete by Id"() {
        def item = new Item(name: 'Spiderman', description: "desc", addedOn: new Date()).save()

        item.delete()

        assert Item.count() == 0
    }

    @Test
    void "should be able to update by annotated Id"() {
        def item = new Item(name: 'Spiderman', description: "desc", addedOn: new Date()).save()

        item.name = "updated_name"
        item.save()

        def result = Item.all()

        assert result.size() == 1
        assert result.first().name == "updated_name"
    }

}

