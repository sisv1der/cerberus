package ru.yarigo.cerberus.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.yarigo.cerberus.TestcontainersConfiguration;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@EnableAsync
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Value("${testing.test_email}")
    private String username;

    @Test
    void sendEmail() throws MessagingException {
        String to = username + "@yandex.ru";
        String subject = "Cerberus_test: " + LocalDateTime.now();
        String body = "Cerberus_test: " + LocalDateTime.now();
        emailService.sendEmail(to, subject, body);

        assertDoesNotThrow(() -> emailService.sendEmail(to, subject, body));
    }
}