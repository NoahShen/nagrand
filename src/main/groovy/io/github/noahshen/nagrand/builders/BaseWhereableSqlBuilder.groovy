package io.github.noahshen.nagrand.builders

import io.github.noahshen.nagrand.builders.query.Query
import io.github.noahshen.nagrand.builders.query.condition.AndCondition
import io.github.noahshen.nagrand.builders.query.condition.Condition
import io.github.noahshen.nagrand.builders.query.condition.Conditions
import io.github.noahshen.nagrand.builders.query.condition.OrCondition
import io.github.noahshen.nagrand.metadata.ClassMetaData

/**
 * represents a builder that can have a where clause
 */
abstract class BaseWhereableSqlBuilder extends BaseSqlBuilder {

    Query queryCondition

    List multiConditionStack

    BaseWhereableSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        queryCondition = new Query()
        multiConditionStack = []
    }

    BaseWhereableSqlBuilder max(int max) {
        queryCondition.max = max
        this
    }

    BaseWhereableSqlBuilder offset(int offset) {
        queryCondition.offset = offset
        this
    }

    BaseWhereableSqlBuilder order(String propertyName) {
        queryCondition.orderBy << Query.Order.desc(propertyName)
        this
    }

    BaseWhereableSqlBuilder order(String propertyName, String direction) {
        queryCondition.orderBy << Query.Order."${direction.toLowerCase()}"(propertyName)
        this
    }

    BaseWhereableSqlBuilder eq(String propertyName, value) {
        addCondition Conditions.eq(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder nq(String propertyName, value) {
        addCondition Conditions.nq(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder inList(String propertyName, List value) {
        addCondition Conditions.'in'(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder 'in'(String propertyName, List value) {
        addCondition Conditions.'in'(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder notIn(String propertyName, List value) {
        addCondition Conditions.notIn(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder like(String propertyName, String expression) {
        addCondition Conditions.like(propertyName, expression)
        this
    }

    BaseWhereableSqlBuilder between(String propertyName, from, to) {
        addCondition Conditions.between(propertyName, from, to)
        this
    }

    BaseWhereableSqlBuilder gt(String propertyName, value) {
        addCondition Conditions.gt(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder lt(String propertyName, value) {
        addCondition Conditions.lt(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder gte(String propertyName, value) {
        addCondition Conditions.gte(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder lte(String propertyName, value) {
        addCondition Conditions.lte(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder isNull(String propertyName) {
        addCondition Conditions.isNull(propertyName)
        this
    }

    BaseWhereableSqlBuilder isNotNull(String propertyName) {
        addCondition Conditions.isNotNull(propertyName)
        this
    }

    BaseWhereableSqlBuilder isEmpty(String propertyName) {
        addCondition Conditions.isEmpty(propertyName)
        this
    }

    BaseWhereableSqlBuilder isNotEmpty(String propertyName) {
        addCondition Conditions.isNotEmpty(propertyName)
        this
    }

    BaseWhereableSqlBuilder eqProperty(String propertyName, String otherProperty) {
        addCondition Conditions.eqProperty(propertyName, otherProperty)
        this
    }

    BaseWhereableSqlBuilder neProperty(String propertyName, String otherProperty) {
        addCondition Conditions.neProperty(propertyName, otherProperty)
        this
    }

    BaseWhereableSqlBuilder gtProperty(String propertyName, String otherProperty) {
        addCondition Conditions.gtProperty(propertyName, otherProperty)
        this
    }

    BaseWhereableSqlBuilder geProperty(String propertyName, String otherProperty) {
        addCondition Conditions.geProperty(propertyName, otherProperty)
        this
    }

    BaseWhereableSqlBuilder ltProperty(String propertyName, String otherProperty) {
        addCondition Conditions.ltProperty(propertyName, otherProperty)
        this
    }

    BaseWhereableSqlBuilder leProperty(String propertyName, String otherProperty) {
        addCondition Conditions.leProperty(propertyName, otherProperty)
        this
    }

    BaseWhereableSqlBuilder and(Closure callable) {
        multiConditionStack << new AndCondition()
        handleMultiCondition(callable)
        this
    }

    BaseWhereableSqlBuilder or(Closure callable) {
        multiConditionStack << new OrCondition()
        handleMultiCondition(callable)
        this
    }


    BaseWhereableSqlBuilder idEq(id) {
        addCondition Conditions.eq(classMetaData.idField.name, id)
        this
    }
    private void handleMultiCondition(Closure callable) {
        try {
            callable.delegate = this
            callable.call()
        } finally {
            def lastCondition = multiConditionStack.pop()
            addCondition lastCondition
        }
    }

    private addCondition(Condition c) {
        if (multiConditionStack) {
            multiConditionStack[-1].addCondition(c)
        } else {
            queryCondition.addCondition(c)
        }
    }
}
