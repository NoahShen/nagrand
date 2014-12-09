package io.github.noahshen.nagrand.spring

import com.mchange.v2.c3p0.ComboPooledDataSource
import io.github.noahshen.nagrand.spring.entity.ItemWithoutEntity
import io.github.noahshen.nagrand.spring.entity.Person
import spock.lang.Specification

import javax.sql.DataSource
/**
 * Created by noahshen on 14-12-9.
 */
class EntityInitializerTest extends Specification {

    EntityInitializer entityInitializerStub

    String entityPackage

    DataSource dataSource

    Boolean createTable

    void setup() {
        entityInitializerStub = new EntityInitializer()
    }
/*
<!-- HSQLDB datasource -->
 <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
  <property name="driverClass" value="org.hsqldb.jdbcDriver" />
  <property name="jdbcUrl" value="jdbc:hsqldb:mem:ex" />
  <property name="user" value="sa" />
  <property name="password" value="" />
 </bean>
 */
    def "Init"() {
        setup:
        entityInitializerStub.createTable = true
        entityInitializerStub.entityPackage = "io.github.noahshen.nagrand.spring.entity"
        entityInitializerStub.sqlLog = true

        ComboPooledDataSource ds = new ComboPooledDataSource()
        ds.driverClass = "org.hsqldb.jdbcDriver"
        ds.jdbcUrl = "jdbc:hsqldb:mem:database"
        ds.user = "sa"
        entityInitializerStub.dataSource = ds


        when:
        entityInitializerStub.init()
        new Person(name: "Noah", age:27).save()

        then:
        def allPersons = Person.all()
        allPersons.size() == 1

        def methods = ItemWithoutEntity.respondsTo("where")
        methods.size() == 0
    }
}
