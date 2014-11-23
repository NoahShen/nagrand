package io.github.noahshen.nagrand.models

import io.github.noahshen.nagrand.annotation.AutoDateCreated
import io.github.noahshen.nagrand.annotation.AutoLastUpdated

class ClassAutoTimestampByAnno {
    Integer id
    String name

    @AutoDateCreated
    Date addTime

    @AutoLastUpdated
    Date updateTime
}
