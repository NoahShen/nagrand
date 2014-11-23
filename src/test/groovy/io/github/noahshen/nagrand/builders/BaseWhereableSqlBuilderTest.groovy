package io.github.noahshen.nagrand.builders

import io.github.noahshen.nagrand.annotation.Id
import io.github.noahshen.nagrand.builders.query.condition.AndCondition
import io.github.noahshen.nagrand.builders.query.condition.Equals
import io.github.noahshen.nagrand.builders.query.condition.OrCondition
import io.github.noahshen.nagrand.metadata.ClassMetaData
import spock.lang.Specification
/**
 * Created by noahshen on 14-11-4.
 */
class BaseWhereableSqlBuilderTest extends Specification {

    BaseWhereableSqlBuilder baseWhereableSqlBuilder

    class Person {
        @Id
        Integer id
        def name
        int age
    }

    ClassMetaData classMetaData

    void setup() {
        classMetaData = new ClassMetaData(Person)
        baseWhereableSqlBuilder = new BaseWhereableSqlBuilderMockTest(classMetaData)
    }

    def "And"() {
        setup:

        when:
        baseWhereableSqlBuilder.eq("name", "Noah").and {
            gte("age", 1)
            lt("age", 20)
        }

        then:
        baseWhereableSqlBuilder.queryCondition.conditions.size() == 2
        baseWhereableSqlBuilder.queryCondition.conditions[-1] instanceof AndCondition
    }

    def "Or"() {
        setup:

        when:
        baseWhereableSqlBuilder.eq("name", "Noah").or {
            lt("age", 18)
            gt("age", 60)
        }

        then:
        baseWhereableSqlBuilder.queryCondition.conditions.size() == 2
        baseWhereableSqlBuilder.queryCondition.conditions[-1] instanceof OrCondition
    }

    def "nested And" () {
        setup:

        when:
        baseWhereableSqlBuilder.eq("name", "Noah")
        baseWhereableSqlBuilder.and {
            eq("age", 10)
            or {
                gte("age", 30)
                lt("age", 40)
            }
        }

        then:
        baseWhereableSqlBuilder.queryCondition.conditions.size() == 2
        baseWhereableSqlBuilder.queryCondition.conditions[-1] instanceof AndCondition
        baseWhereableSqlBuilder.queryCondition.conditions[-1].conditions[-1] instanceof OrCondition
    }


    def "IdEq"() {
        setup:

        when:
        baseWhereableSqlBuilder.idEq(123)

        then:
        baseWhereableSqlBuilder.queryCondition.conditions.size() == 1
        baseWhereableSqlBuilder.queryCondition.conditions[0] instanceof Equals
    }

    private class BaseWhereableSqlBuilderMockTest extends BaseWhereableSqlBuilder {

        BaseWhereableSqlBuilderMockTest(ClassMetaData classMetaData) {
            super(classMetaData)
        }

        @Override
        BuildResult buildSqlAndValues() {
            return null
        }
    }
}
