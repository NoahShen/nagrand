package io.github.noahshen.nagrand.metadata

import io.github.noahshen.nagrand.annotation.AutoDateCreated
import io.github.noahshen.nagrand.annotation.AutoLastUpdated
import io.github.noahshen.nagrand.annotation.Id
import io.github.noahshen.nagrand.annotation.Table

import java.lang.reflect.Field

class ClassMetaData {
    final Class modelClass
    final String tableName
    final FieldMetaData idField
    final FieldMetaData dateCreatedField
    final FieldMetaData lastUpdatedField
    final FieldMetaData versionField

    private final List<FieldMetaData> fields
    private final List<FieldMetaData> allFields
    private Map _fieldsCache        // just to avoid iterating over list of fields and finding by name.

    ClassMetaData(Class modelClass) {
        this.modelClass = modelClass
        this.tableName = extractTableName(modelClass)
        this.idField = getIdFieldOfClass(modelClass)
        this.dateCreatedField = getCreatedDateField(modelClass)
        this.lastUpdatedField = getUpdatedDateField(modelClass)
        this.versionField = getVersionFieldOfClass(modelClass)
        this.fields = getOtherFieldsOfClass(modelClass)

        this.allFields = getAllFieldsOfClass(idField, fields)
        this._fieldsCache = this.allFields.collectEntries { fieldMetaData -> [fieldMetaData.name, fieldMetaData] }
    }

    FieldMetaData getAt(String fieldName) {
        this._fieldsCache[fieldName]
    }

    List<FieldMetaData> getFields() {
        Collections.unmodifiableList(this.fields);
    }

    List<FieldMetaData> getAllFields() {
        Collections.unmodifiableList(this.allFields);
    }

    List<String> getFieldNames() {
        Collections.unmodifiableList(this.fields*.name);
    }

    String getIdFieldName() {
        idField?.name
    }

    private String extractTableName(Class modelClass) {
        modelClass.getAnnotation(Table)?.name()?.trim() ?: modelClass.simpleName
    }


    private List<FieldMetaData> getOtherFieldsOfClass(Class modelClass) {
        fieldsDeclaredIn(modelClass)
                .findAll { !it.isAnnotationPresent(Id) && it.name != "id" }
                .collect { field -> new FieldMetaData(field) }
    }

    private List<FieldMetaData> getAllFieldsOfClass(FieldMetaData idField, List otherFields) {
        def allFields = [idField] + otherFields
        allFields
    }

    private FieldMetaData getIdFieldOfClass(Class modelClass) {
        def idField = fieldsDeclaredIn(modelClass).find { it.isAnnotationPresent(Id) || it.name == "id" }
        if (idField) {
            return new FieldMetaData(idField)
        }
        // add new property
        modelClass.metaClass.id = null
        new FieldMetaData(Integer, "id", "ID")
    }

    private List<Field> fieldsDeclaredIn(Class modelClass) {
        modelClass.declaredFields.findAll { !it.synthetic }
    }

    private FieldMetaData getCreatedDateField(Class modelClass) {
        def field = fieldsDeclaredIn(modelClass).find {
            AutoDateCreated dateCreatedAnno = it.getAnnotation(AutoDateCreated)
            if (dateCreatedAnno && dateCreatedAnno.autoTimestamp()) {
                return it
            }
            if (it.name == "dateCreated" && (!dateCreatedAnno || dateCreatedAnno.autoTimestamp())) {
                return it
            }
        }
        field ? new FieldMetaData(field) : null

    }

    private FieldMetaData getUpdatedDateField(Class modelClass) {
        def field = fieldsDeclaredIn(modelClass).find {
            AutoLastUpdated lastUpdatedAnno = it.getAnnotation(AutoLastUpdated)
            if (lastUpdatedAnno && lastUpdatedAnno.autoTimestamp()) {
                return it
            }
            if (it.name == "lastUpdated" && (!lastUpdatedAnno || lastUpdatedAnno.autoTimestamp())) {
                return it
            }
        }
        field ? new FieldMetaData(field) : null
    }

    private FieldMetaData getVersionFieldOfClass(Class modelClass) {
        def field = fieldsDeclaredIn(modelClass).find {
            if (it.name == "version") {
                return it
            }
        }
        field ? new FieldMetaData(field) : null
    }

}
