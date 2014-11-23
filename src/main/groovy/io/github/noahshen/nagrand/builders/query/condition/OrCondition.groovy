package io.github.noahshen.nagrand.builders.query.condition

/**
 * Created by noahshen on 14-10-26.
 */
class OrCondition extends MultiCondition {
    OrCondition(Condition... conditions) {
        super(conditions)
    }
}
