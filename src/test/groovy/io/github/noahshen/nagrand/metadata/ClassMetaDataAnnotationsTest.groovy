package io.github.noahshen.nagrand.metadata
import io.github.noahshen.nagrand.models.ClassWithIdAnnotation
import io.github.noahshen.nagrand.models.ClassWithTable

class ClassMetaDataAnnotationsTest extends GroovyTestCase {

    void "test tableName"() {
        ClassMetaData metadataWithTable = new ClassMetaData(ClassWithTable)

        assert metadataWithTable.tableName.equalsIgnoreCase("TestTable")
    }

    void "test Id"() {
        ClassMetaData metadataWithId = new ClassMetaData(ClassWithIdAnnotation)

        assert metadataWithId.idFieldName.equals("uid")
        assert metadataWithId.idField.name.equals("uid")
    }


    void "test Id should not be included in fields"() {
        ClassMetaData metadataWithId = new ClassMetaData(ClassWithIdAnnotation)

        assert !metadataWithId.fieldNames.contains("uid")
    }

}
