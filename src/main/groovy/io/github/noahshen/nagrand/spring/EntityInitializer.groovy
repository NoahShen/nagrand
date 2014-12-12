package io.github.noahshen.nagrand.spring

import groovy.sql.Sql
import io.github.noahshen.nagrand.Nagrand
import io.github.noahshen.nagrand.annotation.Entity
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

        nagrand = new Nagrand(new Sql(dataSource))
        if (sqlLog) {
            nagrand.enableQueryLogging(Level.INFO)
        }
        allEntityClass.each {
            nagrand.register(it, createTable)
        }
    }
}
