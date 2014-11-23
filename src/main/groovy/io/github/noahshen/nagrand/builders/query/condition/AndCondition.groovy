package io.github.noahshen.nagrand.builders.query.condition

/**
 * Created by noahshen on 14-10-26.
 */
class AndCondition extends MultiCondition {
    AndCondition(Condition... conditions) {
        super(conditions)
    }
}
