package ru.yarigo.cerberus.auth.web.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yarigo.cerberus.auth.service.AuthService;
import ru.yarigo.cerberus.auth.web.dto.LoginRequest;

@RestController
@RequestMapping ("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @NotNull @RequestBody LoginRequest loginRequest) throws JOSEException, BadCredentialsException {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }
}
