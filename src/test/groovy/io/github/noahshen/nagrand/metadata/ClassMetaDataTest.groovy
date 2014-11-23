package io.github.noahshen.nagrand.metadata

import io.github.noahshen.nagrand.models.Item

class ClassMetaDataTest extends GroovyTestCase {

    ClassMetaData metadata

    void setUp() {
        metadata = new ClassMetaData(Item)
    }

    void "test should create MetaData for subject"() {
        assertNotNull(metadata)
    }

    void "test tableName"() {
        assert metadata.tableName.equalsIgnoreCase("Item")
    }

    void "test field are unmodifiable externally"() {
        shouldFail(UnsupportedOperationException) { metadata.fields << ["name"] }
    }

    void "test persistent field names"() {
        assert metadata.fields*.name == ["name", "description", "addedOn"]
    }

    void "test persistent field names by accessor"() {
        assert metadata.fieldNames == ["name", "description", "addedOn"]
    }

    void "test persistent field types"() {
        assert metadata.fields*.clazz == [String, Object, Date]
    }

    void "test column names"() {
        assert metadata.fields*.columnName == ["name", "description", "addedOn"]
    }


    void "test map like access to field"() {
        assert metadata["description"].columnName == "description"
        assert metadata["description"].clazz == Object
    }

}
