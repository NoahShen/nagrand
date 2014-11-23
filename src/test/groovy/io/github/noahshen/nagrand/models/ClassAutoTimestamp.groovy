package io.github.noahshen.nagrand.models

import io.github.noahshen.nagrand.annotation.Column

class ClassAutoTimestamp {
    Integer id
    String name
    @Column(name = "addTime")
    Date dateCreated

    @Column(name = "updateTime")
    Date lastUpdated
}
