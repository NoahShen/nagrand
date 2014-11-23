package io.github.noahshen.nagrand.builders.query.condition

/**
 * Created by noahshen on 14-10-29.
 */
class GreaterThanProperty extends PropertyComparisonCondition {
    GreaterThanProperty(String propertyName, String otherProperty) {
        super(propertyName, otherProperty)
    }
}
