package ru.yarigo.cerberus.service;

import org.apache.coyote.BadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.yarigo.cerberus.mapper.UserMapper;
import ru.yarigo.cerberus.persistence.model.Profile;
import ru.yarigo.cerberus.persistence.model.Role;
import ru.yarigo.cerberus.persistence.model.User;
import ru.yarigo.cerberus.persistence.repository.ProfileRepository;
import ru.yarigo.cerberus.persistence.repository.RoleRepository;
import ru.yarigo.cerberus.persistence.repository.UserRepository;
import ru.yarigo.cerberus.web.dto.RegisterRequest;
import ru.yarigo.cerberus.web.dto.RegisterResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

class AdminServiceTest {

    private AdminService adminService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ProfileRepository profileRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.roleRepository = Mockito.mock(RoleRepository.class);
        this.profileRepository = Mockito.mock(ProfileRepository.class);
        this.userMapper = Mockito.mock(UserMapper.class);
        this.passwordEncoder = Mockito.mock(PasswordEncoder.class);
        this.adminService = new AdminService(userRepository, roleRepository, profileRepository, passwordEncoder, userMapper);
    }

    @Test
    void registerUser_shouldCreateNewUser() throws BadRequestException {
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
                .passwordHash(passwordEncoder.encode(UUID.randomUUID().toString()))
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

        Mockito.when(roleRepository.findByNameIn(roles))
                .thenReturn(expectedRoles);
        Mockito.when(userRepository.findByUsername(request.username()))
                .thenReturn(Optional.empty());
        Mockito.when(profileRepository.save(any()))
                .thenReturn(expectedProfile);
        Mockito.when(userMapper.userAndProfileToRegisterResponse(any(), any()))
                .thenReturn(expectedResponse);

        var result = adminService.registerUser(request);
        Assertions.assertThat(result.username()).isEqualTo(request.username());

        Mockito.verify(profileRepository, Mockito.times(1))
                .save(any());
    }
}