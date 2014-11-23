package io.github.noahshen.nagrand.metadata
import io.github.noahshen.nagrand.models.ClassWithColumnAnnotation

class ClassMetaDataWithColumnAnnoTest extends GroovyTestCase {

    ClassMetaData metadata

    void setUp() {
        metadata = new ClassMetaData(ClassWithColumnAnnotation)
    }

    void "test should create MetaData for subject"() {
        assertNotNull(metadata)
    }

    void "test tableName"() {
        assert metadata.tableName.equalsIgnoreCase("ClassWithColumnAnnotation")
    }

    void "test field are unmodifiable externally"() {
        shouldFail(UnsupportedOperationException) { metadata.fields << ["name"] }
    }

    void "test persistent field names"() {
        assert metadata.fields*.name == ["name"]
    }

    void "test persistent field names by accessor"() {
        assert metadata.fieldNames == ["name"]
    }

    void "test persistent field types"() {
        assert metadata.fields*.clazz == [String]
    }

    void "test column names"() {
        assert metadata.fields*.columnName == ["fullName"]
    }


    void "test map like access to field"() {
        assert metadata["name"].columnName == "fullName"
    }

}
