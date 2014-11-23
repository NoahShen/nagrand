package io.github.noahshen.nagrand.builders.query.condition

/**
 * Created by noahshen on 14-10-30.
 */
class In extends PropertyValueCondition {

    In(String propertyName, List value) {
        super(propertyName, value)
    }

}
