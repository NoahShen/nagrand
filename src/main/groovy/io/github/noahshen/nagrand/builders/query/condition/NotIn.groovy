package io.github.noahshen.nagrand.builders.query.condition

/**
 * Created by noahshen on 14-10-30.
 */
class NotIn extends PropertyValueCondition {

    NotIn(String propertyName, List value) {
        super(propertyName, value)
    }

}
