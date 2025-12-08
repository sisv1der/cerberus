package ru.yarigo.cerberus.auth.service;

import com.nimbusds.jose.JOSEException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yarigo.cerberus.auth.security.JwtService;
import ru.yarigo.cerberus.auth.security.UserSecurity;
import ru.yarigo.cerberus.users.user.service.UserService;
import ru.yarigo.cerberus.auth.web.dto.LoginRequest;
import ru.yarigo.cerberus.auth.web.dto.LoginResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse authenticate(LoginRequest loginRequest) throws JOSEException, BadCredentialsException, EntityNotFoundException {
        var user = userService.findByUsername(loginRequest.username())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new LoginResponse(jwtService.generateToken(new UserSecurity(user)));
    }
}
