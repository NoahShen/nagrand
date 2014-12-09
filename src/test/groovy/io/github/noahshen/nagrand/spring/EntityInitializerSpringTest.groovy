package io.github.noahshen.nagrand.spring

import io.github.noahshen.nagrand.spring.entity.ItemWithoutEntity
import io.github.noahshen.nagrand.spring.entity.Person
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(locations = [
        "classpath*:/config/spring/local/application-context.xml"
])
class EntityInitializerSpringTest {

    @Autowired
    EntityInitializer entityInitializer;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testInit() throws Exception {
        entityInitializer.init()
        new Person(name: "Noah", age:27).save()

        def allPersons = Person.all()
        assert allPersons.size() == 1

        def methods = ItemWithoutEntity.respondsTo("where")
        assert methods.size() == 0
    }
}