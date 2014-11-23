package io.github.noahshen.nagrand.enhance

import spock.lang.Specification
/**
 * Created by noahshen on 14-10-7.
 */
class DynamicFindEnhancerTest extends Specification {

    def setup() {

    }

    def "Parse one field"() {
        setup:
        DynamicFindEnhancer dsl = new DynamicFindEnhancer()
        when:
        def act = dsl.parseTail('Name')
        then:
        act == ['name']
    }

    def "Parse two fields"() {
        setup:
        DynamicFindEnhancer dsl = new DynamicFindEnhancer()
        when:
        def act = dsl.parseTail('NameAndPassword')
        then:
        act == ['name', 'password']
    }

    def "Parse two long fields"() {
        setup:
        DynamicFindEnhancer dsl = new DynamicFindEnhancer()
        when:
        def act = dsl.parseTail('NameAndFullName')
        then:
        act == ['name', 'fullName']
    }

    def "Parse fields containing and"() {
        setup:
        DynamicFindEnhancer dsl = new DynamicFindEnhancer()
        when:
        def act = dsl.parseTail('AndrewAndCommand')
        then:
        act == ['andrew', 'command']
    }

    def "Parse unusual fields"() {
        setup:
        DynamicFindEnhancer dsl = new DynamicFindEnhancer()
        when:
        def act = dsl.parseTail('XAndYAndZ')
        then:
        act == ['x', 'y', 'z']
        when:
        act = dsl.parseTail('X')
        then:
        act == ['x']
        when:
        act = dsl.parseTail('I18nAndA16z')
        then:
        act == ['i18n', 'a16z']
    }

    def "Build where for one field"() {
        setup:
        DynamicFindEnhancer dsl = new DynamicFindEnhancer()
        when:
        def act = dsl.buildWhere(['name'], ['John'])
        then:
        act == [
                name: 'John'
        ]
    }

    def "Build where for few fields"() {
        setup:
        DynamicFindEnhancer dsl = new DynamicFindEnhancer()
        when:
        def act = dsl.buildWhere(['name', 'age'], ['John', 23])
        then:
        act == [
                name: 'John',
                age: 23
        ]
        when:
        act = dsl.buildWhere(['name', 'age'], ['John', 23, true])
        then:
        act == [
                name: 'John',
                age: 23
        ]
        when:
        act = dsl.buildWhere(['name', 'age', 'email'], ['John', 23])
        then:
        act == [
                name: 'John',
                age: 23
        ]
    }

    def "Get extra"() {
        setup:
        DynamicFindEnhancer dsl = new DynamicFindEnhancer()
        when:
        def act = dsl.getExtra(['name'], ['John'])
        then:
        act.empty
        when:
        act = dsl.getExtra(['name'], ['John', [limit: 4]])
        then:
        act == [
                [limit: 4]
        ]
        when:
        act = dsl.getExtra(['name'], ['John', [limit: 4], { -> println "Hi!"}])
        then:
        act.size() == 2
        act[0] == [limit: 4]
        act[1] instanceof Closure
        when:
        act = dsl.getExtra(['name', 'zip'], ['John', 10032])
        then:
        act.empty
        when:
        act = dsl.getExtra(['name', 'zip'], ['John', 10032, [limit: 4]])
        then:
        act == [
                [limit: 4]
        ]
        when:
        act = dsl.getExtra(['name', 'zip'], ['John', 10032, [limit: 4], { -> println "Hi!"}])
        then:
        act.size() == 2
        act[0] == [limit: 4]
        act[1] instanceof Closure
    }
}
