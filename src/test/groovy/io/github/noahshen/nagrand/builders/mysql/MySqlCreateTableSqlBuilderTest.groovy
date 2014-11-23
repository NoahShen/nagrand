package io.github.noahshen.nagrand.builders.mysql

import io.github.noahshen.nagrand.metadata.ClassMetaData
/**
 * Created by noahshen on 14-10-19.
 */
class MySqlCreateTableSqlBuilderTest extends GroovyTestCase {

    class Person {
        def name
        int age
    }

    MySqlCreateTableSqlBuilder builder
    def classMetaData

    void setUp() {
        classMetaData = new ClassMetaData(Person)
        builder = new MySqlCreateTableSqlBuilder(classMetaData)
    }

    void "test generate mysql create sql"() {
        def result =  builder.buildSqlAndValues()
        assert result.sql == "CREATE TABLE IF NOT EXISTS `Person` (`ID` INT(11) NOT NULL AUTO_INCREMENT, `name` VARCHAR(255), `age` INT(11), PRIMARY KEY (`ID`))"
    }
}
