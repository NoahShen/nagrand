package io.github.noahshen.nagrand.transform
import groovy.sql.Sql
import io.github.noahshen.nagrand.Nagrand
import io.github.noahshen.nagrand.models.PersonForTransform
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.tools.ast.TransformTestHelper

import java.util.logging.Level
/**
 * Created by noahshen on 14-11-18.
 */
class WhereableASTTransformationTest extends GroovyTestCase {

    Nagrand nagrand
    Sql sql

    @Override
    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        nagrand = new Nagrand(sql)
        nagrand.enableQueryLogging(Level.INFO)
        nagrand.stormify(PersonForTransform, true)
    }

    @Override
    void tearDown() {
        sql.execute("drop table PersonForTransform")
        sql.close()
    }

    void testEqCondition() {
        new PersonForTransform(firstName: "Noah", lastName: "Shen", age: 27).save()
        String codes = """
import io.github.noahshen.nagrand.models.PersonForTransform

class PersonService {

    List<PersonForTransform> findPerson(Integer personAge) {
        PersonForTransform.find {
            age == personAge
        }
    }
}
"""


        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(codes)
        def personService = clazz.newInstance()

        def persons = personService.findPerson(27)
        assert persons.size == 1
        assert persons[0].firstName == "Noah"
    }

    void testEqPropertyCondition() {
        new PersonForTransform(firstName: "Noah", lastName: "Noah", age: 27).save()

        String codes = """
import io.github.noahshen.nagrand.models.PersonForTransform

class PersonService {

    List<PersonForTransform> findPersonFirstNameEqLastName() {
        PersonForTransform.find {
            firstName == lastName
        }
    }
}
"""

        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(codes)
        def personService = clazz.newInstance()

        def persons = personService.findPersonFirstNameEqLastName()
        assert persons.size == 1
        assert persons[0].firstName == "Noah"
    }


    void testNqCondition() {
        new PersonForTransform(firstName: "Noah", lastName: "Shen", age: 27).save()

        String codes = """
import io.github.noahshen.nagrand.models.PersonForTransform

class PersonService {

    List<PersonForTransform> findNotEquals(Integer excludeAge) {
        PersonForTransform.where {
            age != excludeAge
        }
    }
}
"""

        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(codes)
        def personService = clazz.newInstance()

        def persons = personService.findNotEquals(30)
        assert persons.size == 1
        assert persons[0].firstName == "Noah"

        def persons2 = personService.findNotEquals(27)
        assert persons2.size == 0
    }

    void testNotEqPropertyCondition() {
        new PersonForTransform(firstName: "Noah", lastName: "Shen", age: 27).save()

        String codes = """
import io.github.noahshen.nagrand.models.PersonForTransform

class PersonService {

    List<PersonForTransform> findPersonFirstNameNotEqLastName() {
        PersonForTransform.find {
            firstName != lastName
        }
    }
}
"""

        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(codes)
        def personService = clazz.newInstance()

        def persons = personService.findPersonFirstNameNotEqLastName()
        assert persons.size == 1
        assert persons[0].firstName == "Noah"
    }


    void testGtCondition() {
        new PersonForTransform(firstName: "Noah", lastName: "Shen", age: 27).save()

        String codes = """
import io.github.noahshen.nagrand.models.PersonForTransform

class PersonService {

    List<PersonForTransform> findFrom(Integer startAge) {
        PersonForTransform.where {
            age > startAge
        }
    }
}
"""

        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(codes)
        def personService = clazz.newInstance()

        def persons = personService.findFrom(20)
        assert persons.size == 1
        assert persons[0].firstName == "Noah"

        def persons2 = personService.findFrom(30)
        assert persons2.size == 0
    }

    void testLtCondition() {
        new PersonForTransform(firstName: "Noah", lastName: "Shen", age: 27).save()

        String codes = """
import io.github.noahshen.nagrand.models.PersonForTransform

class PersonService {

    List<PersonForTransform> findEnd(Integer endAge) {
        PersonForTransform.where {
            age < endAge
        }
    }
}
"""

        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(codes)
        def personService = clazz.newInstance()

        def persons = personService.findEnd(30)
        assert persons.size == 1
        assert persons[0].firstName == "Noah"

        def persons2 = personService.findEnd(20)
        assert persons2.size == 0
    }

    void testInListCondition() {
        new PersonForTransform(firstName: "Noah", lastName: "Shen", age: 27).save()

        String codes = """
import io.github.noahshen.nagrand.models.PersonForTransform

class PersonService {

    List<PersonForTransform> findAgeIn(def ageList) {
        PersonForTransform.where {
            age in ageList
        }
    }
}
"""

        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(codes)
        def personService = clazz.newInstance()

        def persons = personService.findAgeIn([22, 27, 28])
        assert persons.size == 1
        assert persons[0].firstName == "Noah"

        def persons2 = personService.findAgeIn(20..30)
        assert persons2.size == 1
        assert persons2[0].firstName == "Noah"

    }

    void testFindFirstCondition() {
        new PersonForTransform(firstName: "Noah", lastName: "Shen", age: 27).save()
        new PersonForTransform(firstName: "Sara", lastName: "Shi", age: 25).save()
        new PersonForTransform(firstName: "Noah", lastName: "Shen2", age: 27).save()

        String codes = """
import io.github.noahshen.nagrand.models.PersonForTransform

class PersonService {

    PersonForTransform findFirstAge(def firstAge) {
        PersonForTransform.findFirst {
            age == firstAge
            order "lastName", "desc"
        }
    }
}
"""

        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(codes)
        def personService = clazz.newInstance()

        def person = personService.findFirstAge(27)
        assert person
        assert person.lastName == "Shen2"

    }

    void testCountCondition() {
        new PersonForTransform(firstName: "Noah", lastName: "Shen", age: 27).save()
        new PersonForTransform(firstName: "Sara", lastName: "Shi", age: 25).save()
        new PersonForTransform(firstName: "Noah", lastName: "Shen2", age: 27).save()

        String codes = """
import io.github.noahshen.nagrand.models.PersonForTransform

class PersonService {

    Integer findCount(def ageCount) {
        PersonForTransform.count {
            age == ageCount
        }
    }
}
"""

        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(codes)
        def personService = clazz.newInstance()

        def count = personService.findCount(27)
        assert count == 2

    }

    void testAndCondition() {
        new PersonForTransform(firstName: "Noah", lastName: "Shen", age: 27).save()
        new PersonForTransform(firstName: "Sara", lastName: "Shi", age: 25).save()
        new PersonForTransform(firstName: "Noah", lastName: "Shen2", age: 27).save()

        String codes = """
import io.github.noahshen.nagrand.models.PersonForTransform

class PersonService {

    def findNameAndAge(String firstNameCondition, String lastNameCondition, def ageCondition) {
        PersonForTransform.where {
            age == ageCondition
            firstName == firstNameCondition && lastName == lastNameCondition
        }
    }
}
"""

        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(codes)
        def personService = clazz.newInstance()

        def persons = personService.findNameAndAge("Noah", "Shen", 27)
        assert persons.size == 1
        assert persons[0].lastName == "Shen"

    }

    void testOrCondition() {
        new PersonForTransform(firstName: "Noah", lastName: "Shen", age: 27).save()
        new PersonForTransform(firstName: "Sara", lastName: "Shi", age: 25).save()
        new PersonForTransform(firstName: "Noah", lastName: "Shen2", age: 27).save()

        String codes = """
import io.github.noahshen.nagrand.models.PersonForTransform

class PersonService {

    def findNameOrAge(String lastNameCondition1, String lastNameCondition2, def ageCondition) {
        PersonForTransform.where {
            age == ageCondition
            lastName == lastNameCondition1 || lastName == lastNameCondition2
        }
    }
}
"""

        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(codes)
        def personService = clazz.newInstance()

        def persons = personService.findNameOrAge("Shen", "Shen2", 27)
        assert persons.size == 2

    }

}
