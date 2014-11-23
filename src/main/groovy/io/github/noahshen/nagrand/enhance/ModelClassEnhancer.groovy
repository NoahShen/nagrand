package io.github.noahshen.nagrand.enhance
import groovy.sql.Sql
import io.github.noahshen.nagrand.builders.SQLBuilderFactory
import io.github.noahshen.nagrand.builders.SQLDialect
import io.github.noahshen.nagrand.metadata.ClassMetaData

class ModelClassEnhancer {
    final ClassMetaData metaData
    final Sql sql

    SQLDialect dialect

    final DynamicFindEnhancer dynamicFindEnhancer = new DynamicFindEnhancer()

    ModelClassEnhancer(ClassMetaData classMetaData, Sql sql, SQLDialect dialect) {
        this.metaData = classMetaData
        this.sql = sql
        this.dialect = dialect
    }

    public void enhance() {
        addStaticDmlMethods()
        addDynamicFindMethods()
        addInstanceDmlMethods()
    }

    private def addStaticDmlMethods() {
        final modelMetaClass = metaData.modelClass.metaClass
        SQLBuilderFactory sqlBuilderFactory = SQLBuilderFactory.getInstance()

        def where = { Closure whereClosure ->
            if (!whereClosure) {
                throw new IllegalArgumentException("no whereClosure")
            }
            def selectSqlBuilder = sqlBuilderFactory.createSelectSqlBuilder(this.dialect, metaData)
            whereClosure.delegate = selectSqlBuilder
            whereClosure.call()
            def buildResult = selectSqlBuilder.buildSqlAndValues()
            sql.rows(buildResult.sql, buildResult.values)
        }

        modelMetaClass.static.where = where
        modelMetaClass.static.findWhere = where
        modelMetaClass.static.find = where

        def firstWhere = { Closure whereClosure ->
            if (!whereClosure) {
                throw new IllegalArgumentException("no whereClosure")
            }
            def selectSqlBuilder = sqlBuilderFactory.createSelectSqlBuilder(this.dialect, metaData)
            whereClosure.delegate = selectSqlBuilder
            whereClosure.call()
            def buildResult = selectSqlBuilder.buildSqlAndValues()
            sql.firstRow(buildResult.sql, buildResult.values)
        }

        modelMetaClass.static.findFirst = firstWhere
        modelMetaClass.static.findFirstWhere = firstWhere

        def getAll = {
            def selectSqlBuilder = sqlBuilderFactory.createSelectSqlBuilder(this.dialect, metaData)
            def buildResult = selectSqlBuilder.buildSqlAndValues()
            sql.rows(buildResult.sql)
        }
        modelMetaClass.static.getAll = getAll
        modelMetaClass.static.all = getAll

        def getCount = { Closure whereClosure ->
            def countSqlBuilder = sqlBuilderFactory.createCountSqlBuilder(this.dialect, metaData)
            if (whereClosure) {
                whereClosure.delegate = countSqlBuilder
                whereClosure.call()
            }
            def buildResult = countSqlBuilder.buildSqlAndValues()
            sql.firstRow(buildResult.sql, buildResult.values).count
        }
        modelMetaClass.static.count = getCount
        modelMetaClass.static.getCount = getCount

        modelMetaClass.static.get = { id ->
            def selectByIdQuery = sqlBuilderFactory.createSelectSqlBuilder(this.dialect, metaData)
            def buildResult = selectByIdQuery.idEq(id).buildSqlAndValues()
            sql.firstRow(buildResult.sql, buildResult.values)
        }
    }

    private def addDynamicFindMethods() {
        final modelMetaClass = metaData.modelClass.metaClass
        modelMetaClass.static.methodMissing = { String name, args ->
            if (dynamicFindEnhancer.supports(name)) {
                return dynamicFindEnhancer.tryExecute(metaData.modelClass, name, (List) args)
            } else {
                new MissingMethodException(name, delegate, args)
            }
        }

    }

    private def addInstanceDmlMethods() {
        final modelMetaClass = metaData.modelClass.metaClass
        SQLBuilderFactory sqlBuilderFactory = SQLBuilderFactory.getInstance()

        String idFieldName = metaData.idFieldName;
        modelMetaClass.save = {
            def entity = delegate
            if (entity."${idFieldName}" == null) {
                fireEventMethod(entity, "beforeInsert")
                autoTimestampWhenInsert(entity, metaData)
                if (metaData.versionField) {
                    entity."${metaData.versionField.name}" = 1
                }
                def insertSqlBuilder = sqlBuilderFactory.createInsertSqlBuilder(this.dialect, metaData, entity)
                def buildResult = insertSqlBuilder.buildSqlAndValues()
                def generatedIds = sql.executeInsert(buildResult.sql, buildResult.values)
                def keyId = generatedIds[0][0] // pretty stupid way to extract it
                entity."${idFieldName}" = keyId
                fireEventMethod(entity, "afterInsert")
            } else {
                fireEventMethod(entity, "beforeUpdate")
                autoTimestampWhenUpdate(entity, metaData)
                Integer oldVersion = null
                if (metaData.versionField) {
                    oldVersion = entity."${metaData.versionField.name}"
                    entity."${metaData.versionField.name}"++
                }
                def updateSqlBuilder = sqlBuilderFactory.createUpdateSqlBuilder(this.dialect, metaData, entity)
                updateSqlBuilder.idEq(entity."${idFieldName}")
                if (metaData.versionField && oldVersion != null) {
                    updateSqlBuilder.eq(metaData.versionField.name, oldVersion)
                }
                def buildResult = updateSqlBuilder.buildSqlAndValues()
                Integer updatedRow = sql.executeUpdate(buildResult.sql, buildResult.values)
                if (updatedRow <= 0) {
                    // update failed, roll back old version
                    if (metaData.versionField && oldVersion != null) {
                        entity."${metaData.versionField.name}" = oldVersion
                    }
                    // not invoke afterUpdate
                    return entity
                }
                fireEventMethod(entity, "afterUpdate")
            }
            entity
        }

        modelMetaClass.delete = {
            def entity = delegate
            if (entity."${idFieldName}" != null) {
                fireEventMethod(entity, "beforeDelete")
                def deleteSqlBuilder = sqlBuilderFactory.createDeleteSqlBuilder(this.dialect, metaData)
                def buildResult = deleteSqlBuilder.idEq(entity."${idFieldName}").buildSqlAndValues()
                sql.execute(buildResult.sql, buildResult.values)
                fireEventMethod(entity, "afterDelete")
            }
            entity
        }
    }


    private void fireEventMethod(def entity, String method) {
        if (entity.metaClass.respondsTo(entity, method)) {
            entity."$method"()
        }
    }

    private void autoTimestampWhenInsert(def entity, ClassMetaData classMetaData) {
        if (classMetaData.dateCreatedField) {
            entity."${classMetaData.dateCreatedField.name}" = new Date()
        }
        if (classMetaData.lastUpdatedField) {
            entity."${classMetaData.lastUpdatedField.name}" = new Date()
        }
    }

    private void autoTimestampWhenUpdate(def entity, ClassMetaData classMetaData) {
        if (classMetaData.lastUpdatedField) {
            entity."${classMetaData.lastUpdatedField.name}" = new Date()
        }
    }
}
