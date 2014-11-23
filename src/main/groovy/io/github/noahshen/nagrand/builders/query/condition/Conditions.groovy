package io.github.noahshen.nagrand.builders.query.condition

/**
 * Created by noahshen on 14-10-30.
 */
class Conditions {

    static AndCondition and(Condition... conditions) {
        new AndCondition(conditions)
    }

    static OrCondition or(Condition... conditions) {
        new OrCondition(conditions)
    }

    static Equals eq(String propertyName, value) {
        new Equals(propertyName, value)
    }

    static NotEquals nq(String propertyName, value) {
        new NotEquals(propertyName, value)
    }

    static In 'in'(String propertyName, List value) {
        new In(propertyName, value)
    }

    static NotIn notIn(String propertyName, List value) {
        new NotIn(propertyName, value)
    }

    static Like like(String propertyName, String expression) {
        new Like(propertyName, expression)
    }

    static Between between(String propertyName, from, to) {
        new Between(propertyName, from ,to)
    }

    static GreaterThan gt(String propertyName, value) {
        new GreaterThan(propertyName, value)
    }

    static LessThan lt(String propertyName, value) {
        new LessThan(propertyName, value)
    }

    static GreaterThanEquals gte(String propertyName, value) {
        new GreaterThanEquals(propertyName, value)
    }

    static LessThanEquals lte(String propertyName, value) {
        new LessThanEquals(propertyName, value)
    }

    static IsNull isNull(String propertyName) {
        new IsNull(propertyName)
    }

    static IsNotNull isNotNull(String propertyName) {
        new IsNotNull(propertyName)
    }

    static IsEmpty isEmpty(String propertyName) {
        new IsEmpty(propertyName)
    }

    static IsNotEmpty isNotEmpty(String propertyName) {
        new IsNotEmpty(propertyName)
    }

    static EqualsProperty eqProperty(String propertyName, String otherProperty) {
        new EqualsProperty(propertyName, otherProperty)
    }

    static NotEqualsProperty neProperty(String propertyName, String otherProperty) {
        new NotEqualsProperty(propertyName, otherProperty)
    }

    static GreaterThanProperty gtProperty(String propertyName, String otherProperty) {
        new GreaterThanProperty(propertyName, otherProperty)
    }

    static GreaterThanEqualsProperty geProperty(String propertyName, String otherProperty) {
        new GreaterThanEqualsProperty(propertyName, otherProperty)
    }

    static LessThanProperty ltProperty(String propertyName, String otherProperty) {
        new LessThanProperty(propertyName, otherProperty)
    }

    static LessThanEqualsProperty leProperty(String propertyName, String otherProperty) {
        new LessThanEqualsProperty(propertyName, otherProperty)
    }
}
