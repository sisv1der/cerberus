package ru.yarigo.cerberus.admin.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import ru.yarigo.cerberus.admin.dto.UserRegistered;
import ru.yarigo.cerberus.admin.web.dto.RegisterRequest;
import ru.yarigo.cerberus.admin.web.dto.RegisterResponse;
import ru.yarigo.cerberus.infrastructure.notification.NotificationService;
import ru.yarigo.cerberus.users.profiles.model.Profile;
import ru.yarigo.cerberus.users.user.model.User;
import ru.yarigo.cerberus.users.user.service.UserService;
import ru.yarigo.cerberus.users.user.web.dto.UserInfo;
import ru.yarigo.cerberus.users.user.web.dto.UserMapper;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final NotificationService emailService;

    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) throws BadRequestException, EntityExistsException {
        String password = UUID.randomUUID().toString();
        UserRegistered userRegistered = userService.createUser(request, password);
        User user = userRegistered.user();
        Profile profile = userRegistered.profile();

        emailService.sendRegistrationMessage(profile.getFullName(), user.getEmail(), user.getUsername(), password, user.getCreatedAt());

        return userMapper.userAndProfileToRegisterResponse(user, profile);
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
