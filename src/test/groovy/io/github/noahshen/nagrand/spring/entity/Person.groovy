package io.github.noahshen.nagrand.spring.entity

import io.github.noahshen.nagrand.annotation.Entity

/**
 * simplest model, just for sanity checks
 */
@Entity
class Person {
    def name
    int age
}
