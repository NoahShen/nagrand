package io.github.noahshen.nagrand.builders.query.condition

/**
 * Created by noahshen on 14-10-28.
 */
class PropertyValueCondition extends PropertyNameCondition {

    def value

    PropertyValueCondition(String propertyName, value) {
        super(propertyName)
        this.value = value
    }

}
