package ru.yarigo.cerberus;

import org.springframework.boot.SpringApplication;

public class TestCerberusApplication {

    public static void main(String[] args) {
        SpringApplication.from(CerberusApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
