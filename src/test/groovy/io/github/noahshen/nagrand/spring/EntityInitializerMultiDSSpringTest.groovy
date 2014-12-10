package io.github.noahshen.nagrand.spring

import io.github.noahshen.nagrand.spring.entitydsA.PersonA
import io.github.noahshen.nagrand.spring.entitydsB.PersonB
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(locations = [
        "classpath*:/config/spring/local/application-context-multi-datasource.xml"
])
class EntityInitializerMultiDSSpringTest {

    @Autowired
    EntityInitializer entityInitializerA;

    @Autowired
    EntityInitializer entityInitializerB;


    @Test
    public void testInit() throws Exception {
        new PersonA(name: "Noah", age:27).save()

        def allPersonsInDSA = PersonA.all()
        assert allPersonsInDSA.size() == 1

        new PersonB(name: "Noah", age:27).save()

        def allPersonsInDSB = PersonB.all()
        assert allPersonsInDSB.size() == 1
    }
}