package io.github.noahshen.nagrand.builders.query.condition

/**
 * Created by noahshen on 14-10-30.
 */
class Between extends PropertyValueCondition {

    def from

    def to

    Between(String propertyName, from, to) {
        super(propertyName, from)
        this.from = from
        this.to = to
    }
}
