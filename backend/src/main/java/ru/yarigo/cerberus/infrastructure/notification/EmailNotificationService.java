package ru.yarigo.cerberus.infrastructure.notification;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yarigo.cerberus.infrastructure.notification.exception.EmailNotificationException;
import ru.yarigo.cerberus.infrastructure.smtp.EmailService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private EmailService emailService;

    @Override
    public void sendRegistrationMessage(String fullName, String email, String username, String password, LocalDateTime registrationTime) {
        sendCreationEmail(fullName, email, username, password, registrationTime);
    }

    private void sendCreationEmail(String fullName, String email, String username, String password, LocalDateTime registrationTime) {
        final String SUBJECT = "Регистрация нового пользователя Cerberus";
        String body = generateRegistrationEmailBody(fullName, username, password, registrationTime);

        try {
            emailService.sendEmail(email, SUBJECT, body);
        } catch (MessagingException e) {
          throw new EmailNotificationException(e.getMessage());
        }
    }

    private String generateRegistrationEmailBody(String fullName, String username, String password, LocalDateTime registrationTime) {
        return String.format(
                """
                Уважаемый/ая %s!
                
                Благодарим вас за регистрацию в Cerberus.
                
                Ваш аккаунт:
                • Логин: %s
                • Пароль: %s
                • Дата регистрации: %s
                
                Для входа в систему используйте ваш email и пароль.
                
                Если у вас возникли вопросы, обратитесь в службу поддержки.
                
                С уважением,
                Команда сервиса Cerberus
                """,
                fullName,
                username,
                password,
                registrationTime
        );
    }
}
