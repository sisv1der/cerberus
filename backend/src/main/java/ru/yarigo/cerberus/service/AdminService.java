package ru.yarigo.cerberus.service;

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

    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) throws BadRequestException {
        Set<Role> roles = Set.copyOf(roleRepository.findByNameIn(request.roles()));
        if (roles.size() != request.roles().size()) {
            throw new BadRequestException("One or more roles not found");
        }

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new BadRequestException("Login already in use");
        }

        String tempPassword = UUID.randomUUID().toString(); //TODO: подключить сюда SMTP для рассылки пароля по почте

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(tempPassword))
                .roles(roles)
                .build();

        Profile profile = new Profile();
        profile.setFullName(request.fullName());

        profile.setUser(user);
        profile = profileRepository.save(profile);

        return userMapper.userAndProfileToRegisterResponse(profile.getUser(), profile);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setActive(false);
            userRepository.save(user);
        }
    }
}
