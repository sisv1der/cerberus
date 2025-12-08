package ru.yarigo.cerberus.service;

import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yarigo.cerberus.admin.dto.UserRegistered;
import ru.yarigo.cerberus.users.user.service.UserService;
import ru.yarigo.cerberus.users.user.web.dto.UserMapper;
import ru.yarigo.cerberus.users.profiles.model.Profile;
import ru.yarigo.cerberus.users.roles.model.Role;
import ru.yarigo.cerberus.users.user.model.User;
import ru.yarigo.cerberus.admin.service.AdminService;
import ru.yarigo.cerberus.infrastructure.smtp.EmailService;
import ru.yarigo.cerberus.admin.web.dto.RegisterRequest;
import ru.yarigo.cerberus.admin.web.dto.RegisterResponse;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

class AdminServiceTest {

    private AdminService adminService;
    private UserMapper userMapper;
    private UserService userService;

    @BeforeEach
    void setUp() {
        this.userService = Mockito.mock(UserService.class);
        this.userMapper = Mockito.mock(UserMapper.class);
        EmailService emailService = Mockito.mock(EmailService.class);
        this.adminService = new AdminService(userService, userMapper, emailService);
    }

    @Test
    void registerUser_shouldCreateNewUser() throws BadRequestException, MessagingException {
        var roles = List.of("ROLE_ADMIN", "ROLE_USER");
        var role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_ADMIN");
        var role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_USER");

        var request = new RegisterRequest("username", "email", roles, "fullName");

        var expectedUser = User.builder()
                .email("email")
                .username("username")
                .roles(Set.of(role1, role2))
                .passwordHash("password")
                .build();
        expectedUser.setId(1L);

        var expectedRoles = List.of(role1, role2);

        var expectedProfile = new Profile();
        expectedProfile.setId(1L);
        expectedProfile.setFullName("fullName");
        expectedProfile.setUser(expectedUser);

        var expectedResponse = new RegisterResponse(
                expectedUser.getId(),
                expectedUser.getUsername(),
                expectedUser.getEmail(),
                expectedRoles.stream().map(Role::getName).toList(),
                expectedProfile.getFullName()
        );

        Mockito.when(userService.createUser(any(), any()))
                        .thenReturn(new UserRegistered(expectedUser, expectedProfile));

        Mockito.when(userMapper.userAndProfileToRegisterResponse(any(), any()))
                .thenReturn(expectedResponse);

        var result = adminService.registerUser(request);
        Assertions.assertThat(result.username()).isEqualTo(request.username());
    }
}