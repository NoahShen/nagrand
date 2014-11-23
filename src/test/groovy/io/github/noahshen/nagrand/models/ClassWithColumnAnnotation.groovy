package io.github.noahshen.nagrand.models

import io.github.noahshen.nagrand.annotation.Column
import io.github.noahshen.nagrand.annotation.Id

class ClassWithColumnAnnotation {
    @Id
    Integer uid

    @Column(name = "fullName")
    String name
}
