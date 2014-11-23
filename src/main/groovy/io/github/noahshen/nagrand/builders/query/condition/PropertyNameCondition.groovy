package io.github.noahshen.nagrand.builders.query.condition

/**
 * Created by noahshen on 14-10-28.
 */
class PropertyNameCondition implements Condition {

    String propertyName;

    PropertyNameCondition(String propertyName) {
        this.propertyName = propertyName
    }

    protected void setPropertyName(String propertyName) {
        this.propertyName = propertyName
    }
}
