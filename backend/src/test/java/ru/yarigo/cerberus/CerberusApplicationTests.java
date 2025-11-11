package ru.yarigo.cerberus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CerberusApplicationTests {

    @Test
    void contextLoads() {
    }

}
