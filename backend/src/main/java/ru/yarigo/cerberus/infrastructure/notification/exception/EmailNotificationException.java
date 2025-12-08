package ru.yarigo.cerberus.infrastructure.notification.exception;

public class EmailNotificationException extends RuntimeException {
    public EmailNotificationException(String message) {
        super(message);
    }
}
