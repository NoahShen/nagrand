package io.github.noahshen.nagrand.models

class ClassWithEvents {
    Integer id
    String name

    def beforeInsert() {
        println "beforeInsert"
        this.metaClass.beforeInsertProperty = true
    }

    def afterInsert() {
        println "afterInsert"
        this.metaClass.afterInsertProperty = true
    }

    def beforeUpdate() {
        println "beforeUpdate"
        this.metaClass.beforeUpdateProperty = true
    }

    def afterUpdate() {
        println "afterUpdate"
        this.metaClass.afterUpdateProperty = true
    }

    def beforeDelete() {
        println "beforeDelete"
        this.metaClass.beforeDeleteProperty = true
    }

    def afterDelete() {
        println "afterDelete"
        this.metaClass.afterDeleteProperty = true
    }
}
