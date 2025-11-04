package ru.yarigo.cerberus.web.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.yarigo.cerberus.service.AdminService;
import ru.yarigo.cerberus.web.dto.RegisterRequest;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest registerRequest) throws BadRequestException {
        var response = adminService.registerUser(registerRequest);

        URI responseUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(responseUri).body(response);
    }
}
