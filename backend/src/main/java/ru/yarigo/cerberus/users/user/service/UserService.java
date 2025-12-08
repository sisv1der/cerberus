package ru.yarigo.cerberus.users.user.service;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yarigo.cerberus.admin.dto.UserRegistered;
import ru.yarigo.cerberus.admin.web.dto.RegisterRequest;
import ru.yarigo.cerberus.users.profiles.model.Profile;
import ru.yarigo.cerberus.users.profiles.service.ProfileService;
import ru.yarigo.cerberus.users.roles.model.Role;
import ru.yarigo.cerberus.users.roles.service.RoleService;
import ru.yarigo.cerberus.users.user.model.User;
import ru.yarigo.cerberus.users.user.model.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ProfileService profileService;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public UserRegistered createUser(RegisterRequest request, String password) throws BadRequestException {
        Set<Role> roles = roleService.findByNameIn(request.roles());
        if (roles.size() != request.roles().size()) {
            throw new BadRequestException("One or more roles not found");
        }

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new EntityExistsException("Username already exists");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(password))
                .roles(roles)
                .build();

        Profile profile = profileService.createProfileForUser(user, request.fullName());

        return new UserRegistered(user, profile);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.findById(id).ifPresent(user -> user.setActive(false));
    }
}
