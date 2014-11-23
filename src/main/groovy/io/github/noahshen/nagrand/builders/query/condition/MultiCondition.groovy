package io.github.noahshen.nagrand.builders.query.condition

/**
 * multi query condition
 */
abstract class MultiCondition implements Condition {

    List<Condition> conditions

    MultiCondition(Condition... conditions) {
        this.conditions = conditions ? conditions.toList() : []
    }

    MultiCondition addCondition(Condition c) {
        if (c) {
            conditions.add(c)
        }
        return this
    }

    protected void setConditions(List<Condition> conditions) {
        this.conditions = conditions
    }
}
