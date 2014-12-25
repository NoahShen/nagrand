package io.github.noahshen.nagrand.spring

import groovy.sql.Sql
import io.github.noahshen.nagrand.Nagrand
import io.github.noahshen.nagrand.annotation.Entity
import io.github.noahshen.nagrand.builders.SQLDialect
import org.reflections.Reflections

import javax.sql.DataSource
import java.util.logging.Level

/**
 * Created by noahshen on 14-12-9.
 */
class EntityInitializer {

    String entityPackage

    DataSource dataSource

    Boolean createTable

    Nagrand nagrand

    Boolean sqlLog

    SQLDialect dialect

    void init() {
        if (!entityPackage) {
            return
        }
        def packages = entityPackage.split(",")
        Set allEntityClass = []
        packages.each {
            Reflections reflections = new Reflections(it.trim())
            allEntityClass.addAll reflections.getTypesAnnotatedWith(Entity)
        }

        if (!allEntityClass) {
            return
        }

        nagrand = new Nagrand(new Sql(dataSource), dialect)
        if (sqlLog) {
            nagrand.enableQueryLogging(Level.INFO)
        }
        allEntityClass.each {
            nagrand.register(it, createTable)
        }
    }
}
