package io.github.noahshen.nagrand.builders.query

import io.github.noahshen.nagrand.builders.query.condition.Condition
/**
 * Created by noahshen on 14-10-26.
 */
class Query {

    Integer max = -1

    Integer offset = 0

    List<Order> orderBy = []

    List<Condition> conditions = []

    Query addCondition(Condition c) {
        if (c) {
            conditions.add(c)
        }
        return this
    }

    static class Order {
        Direction direction = Direction.ASC;
        String property;

        static Order desc(String property) {
            return new Order(property: property, direction: Direction.DESC);
        }

        static Order asc(String property) {
            return new Order(property: property, direction: Direction.ASC);
        }

        static enum Direction {
            ASC, DESC
        }
    }
}
