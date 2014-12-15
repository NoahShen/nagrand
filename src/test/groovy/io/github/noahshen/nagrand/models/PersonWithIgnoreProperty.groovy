package io.github.noahshen.nagrand.models

import io.github.noahshen.nagrand.annotation.Entity
import io.github.noahshen.nagrand.annotation.Ignore

/**
 *
 */
@Entity
class PersonWithIgnoreProperty {
    def name
    int age

    @Ignore
    def memo
}
