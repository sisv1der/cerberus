package ru.yarigo.cerberus.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yarigo.cerberus.mapper.UserMapper;
import ru.yarigo.cerberus.persistence.model.Profile;
import ru.yarigo.cerberus.persistence.model.Role;
import ru.yarigo.cerberus.persistence.model.User;
import ru.yarigo.cerberus.persistence.repository.ProfileRepository;
import ru.yarigo.cerberus.persistence.repository.RoleRepository;
import ru.yarigo.cerberus.persistence.repository.UserRepository;
import ru.yarigo.cerberus.web.dto.RegisterRequest;
import ru.yarigo.cerberus.web.dto.RegisterResponse;
import ru.yarigo.cerberus.web.dto.UserInfo;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) throws BadRequestException {
        Set<Role> roles = Set.copyOf(roleRepository.findByNameIn(request.roles()));
        if (roles.size() != request.roles().size()) {
            throw new BadRequestException("One or more roles not found");
        }

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new BadRequestException("Login already in use");
        }

        String password = UUID.randomUUID().toString();

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(password))
                .roles(roles)
                .build();

        Profile profile = new Profile();
        profile.setFullName(request.fullName());

        profile.setUser(user);
        profile = profileRepository.save(profile);

        sendCreationEmail(profile.getFullName(), user.getEmail(), user.getUsername(), password, user.getCreatedAt());

        return userMapper.userAndProfileToRegisterResponse(profile.getUser(), profile);
    }

    private void sendCreationEmail(String fullName, String email, String username, String password, LocalDateTime registrationTime) {
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
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setActive(false);
            userRepository.save(user);
        }
    }

    @Transactional
    public UserInfo getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userMapper.userAndProfileToUserInfo(user, user.getProfile());
    }
}
