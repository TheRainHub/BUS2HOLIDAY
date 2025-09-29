package cz.cvut.ear.bus2holiday;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class Bus2holidayApplicationTests {

    @Test
    void contextLoads() {
    }

}
