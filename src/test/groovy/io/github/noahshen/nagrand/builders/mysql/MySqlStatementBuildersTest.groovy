package io.github.noahshen.nagrand.builders.mysql

import io.github.noahshen.nagrand.annotation.Column
import io.github.noahshen.nagrand.builders.query.Query
import io.github.noahshen.nagrand.builders.query.condition.*
import io.github.noahshen.nagrand.metadata.ClassMetaData
import spock.lang.Specification

/**
 * Created by noahshen on 14-11-6.
 */
class MySqlStatementBuildersTest extends Specification {

    class Person {
        Integer id
        @Column(name = "PersonName")
        def name

        @Column(name = "PersonAge")
        int age
    }

    ClassMetaData classMetaData

    void setup() {
        classMetaData = new ClassMetaData(Person.class)
    }


    def "test Equals"() {
        setup:
        def values = []
        Equals e = new Equals("name", "Noah")

        when:
        def sql = MySqlStatementBuilders.generateConditionSql(e, classMetaData, values)

        then:
        sql == "`PersonName` = ?"
        values.size() == 1
        values[0] == "Noah"
    }

    def "test In Condition"() {
        setup:
        def values = []
        In i = new In("name", ["Noah1", "Noah2", "Noah3"])

        when:
        def sql = MySqlStatementBuilders.generateConditionSql(i, classMetaData, values)

        then:
        sql == "`PersonName` IN (?, ?, ?)"
        values.size() == 3
        values[0] == "Noah1"
        values[1] == "Noah2"
        values[2] == "Noah3"
    }

    def "test Not In Condition"() {
        setup:
        def values = []
        NotIn i = new NotIn("name", ["Noah1", "Noah2", "Noah3"])

        when:
        def sql = MySqlStatementBuilders.generateConditionSql(i, classMetaData, values)

        then:
        sql == "`PersonName` NOT IN (?, ?, ?)"
        values.size() == 3
        values[0] == "Noah1"
        values[1] == "Noah2"
        values[2] == "Noah3"
    }

    def "test Between In Condition"() {
        setup:
        def values = []
        Between between = new Between("age", 10, 20)

        when:
        def sql = MySqlStatementBuilders.generateConditionSql(between, classMetaData, values)

        then:
        sql == "`PersonAge` BETWEEN ? AND ?"
        values.size() == 2
        values[0] == 10
        values[1] == 20
    }

    def "test And Condition"() {
        setup:
        def values = []
        Equals e = new Equals("name", "Noah")
        Between between = new Between("age", 10, 20)
        AndCondition andCondition = new AndCondition(e, between)

        when:
        def sql = MySqlStatementBuilders.generateConditionSql(andCondition, classMetaData, values)

        then:
        sql == "( `PersonName` = ? AND `PersonAge` BETWEEN ? AND ? )"
        values.size() == 3
        values[0] == "Noah"
        values[1] == 10
        values[2] == 20
    }

    def "test Or Condition"() {
        setup:
        def values = []
        Equals e = new Equals("name", "Noah")
        Between between = new Between("age", 10, 20)
        OrCondition orCondition = new OrCondition(e, between)

        when:
        def sql = MySqlStatementBuilders.generateConditionSql(orCondition, classMetaData, values)

        then:
        sql == "( `PersonName` = ? OR `PersonAge` BETWEEN ? AND ? )"
        values.size() == 3
        values[0] == "Noah"
        values[1] == 10
        values[2] == 20
    }

    def "test EqualsProperty Condition"() {
        setup:
        def values = []
        EqualsProperty equalsProperty = new EqualsProperty("name", "age")

        when:
        def sql = MySqlStatementBuilders.generateConditionSql(equalsProperty, classMetaData, values)

        then:
        sql == "`PersonName` = PersonAge"
        values.size() == 0
    }


    def "test IsEmpty Condition"() {
        setup:
        def values = []
        IsEmpty isEmpty = new IsEmpty("name")

        when:
        def sql = MySqlStatementBuilders.generateConditionSql(isEmpty, classMetaData, values)

        then:
        sql == "`PersonName` = ''"
        values.size() == 0
    }

    def "test Order Condition"() {
        setup:
        def values = []
        Query query = new Query()
        query.orderBy << Query.Order.desc("name")

        when:
        def sql = MySqlStatementBuilders.generateQuerySql(query, classMetaData, values)

        then:
        sql == " ORDER BY `PersonName` DESC"
        values.size() == 0
    }

}
