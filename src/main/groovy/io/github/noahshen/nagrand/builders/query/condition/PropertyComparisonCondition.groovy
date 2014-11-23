package io.github.noahshen.nagrand.builders.query.condition

/**
 * Created by noahshen on 14-10-28.
 */
class PropertyComparisonCondition extends PropertyNameCondition {

    String otherProperty;

    PropertyComparisonCondition(String propertyName, String otherProperty) {
        super(propertyName)
        this.otherProperty = otherProperty
    }

    protected void setOtherProperty(String otherProperty) {
        this.otherProperty = otherProperty
    }
}
