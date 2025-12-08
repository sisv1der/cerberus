package ru.yarigo.cerberus.admin.service;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import ru.yarigo.cerberus.admin.dto.UserRegistered;
import ru.yarigo.cerberus.admin.web.dto.RegisterRequest;
import ru.yarigo.cerberus.admin.web.dto.RegisterResponse;
import ru.yarigo.cerberus.infrastructure.smtp.EmailService;
import ru.yarigo.cerberus.users.profiles.model.Profile;
import ru.yarigo.cerberus.users.user.model.User;
import ru.yarigo.cerberus.users.user.service.UserService;
import ru.yarigo.cerberus.users.user.web.dto.UserInfo;
import ru.yarigo.cerberus.users.user.web.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) throws BadRequestException, EntityExistsException, MessagingException {
        String password = UUID.randomUUID().toString();
        UserRegistered userRegistered = userService.createUser(request, password);
        User user = userRegistered.user();
        Profile profile = userRegistered.profile();

        sendCreationEmail(profile.getFullName(), user.getEmail(), user.getUsername(), password, user.getCreatedAt());

        return userMapper.userAndProfileToRegisterResponse(user, profile);
    }

    private void sendCreationEmail(String fullName, String email, String username, String password, LocalDateTime registrationTime) throws MessagingException {
        final String SUBJECT = "Регистрация нового пользователя Cerberus";
        String body = generateRegistrationEmailBody(fullName, username, password, registrationTime);

        emailService.sendEmail(email, SUBJECT, body);
    }

    private String generateRegistrationEmailBody(String fullName, String username, String password, LocalDateTime registrationTime) {
        return String.format(
                """
                Уважаемый/ая %s!
                
                Благодарим вас за регистрацию в нашем сервисе.
                
                Ваш аккаунт:
                • Логин: %s
                • Пароль: %s
                • Дата регистрации: %s
                
                Для входа в систему используйте ваш email и пароль.
                
                Если у вас возникли вопросы, обратитесь в службу поддержки.
                
                С уважением,
                Команда Вашего Сервиса
                """,
                fullName,
                username,
                password,
                registrationTime
        );
    }

    @Transactional
    public void deleteUser(Long userId) {
        userService.deleteById(userId);
    }

    @Transactional
    public UserInfo getUserInfo(Long userId) throws EntityNotFoundException {
        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userMapper.userAndProfileToUserInfo(user, user.getProfile());
    }
}
