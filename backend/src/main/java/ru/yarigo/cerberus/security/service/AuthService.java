package ru.yarigo.cerberus.security.service;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.yarigo.cerberus.security.model.UserSecurity;
import ru.yarigo.cerberus.service.UserService;
import ru.yarigo.cerberus.web.dto.LoginRequest;
import ru.yarigo.cerberus.web.dto.LoginResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse authenticate(LoginRequest loginRequest) throws JOSEException {
        var user = userService.findByUsername(loginRequest.username());

        if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new LoginResponse(jwtService.generateToken(new UserSecurity(user)));
    }
}
