package ru.yarigo.cerberus.web.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.yarigo.cerberus.service.AdminService;
import ru.yarigo.cerberus.web.dto.RegisterRequest;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Validated
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@Valid @NotNull @RequestBody RegisterRequest registerRequest) throws BadRequestException, MessagingException {
        var response = adminService.registerUser(registerRequest);

        URI responseUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(responseUri).body(response);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<?> deleteUser(@Min(1) @PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserInfo(@Min(1) @PathVariable Long id) {
        return ResponseEntity.ok().body(adminService.getUserInfo(id));
    }
}
