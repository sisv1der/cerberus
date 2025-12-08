package ru.yarigo.cerberus.infrastructure.notification;

import java.time.LocalDateTime;

public interface NotificationService {

    void sendRegistrationMessage(String fullName, String email, String username, String password, LocalDateTime registrationTime);
}
