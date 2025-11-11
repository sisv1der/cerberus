package ru.yarigo.cerberus.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RegisterRequest(
        @NotNull String username,
        @NotNull @Email String email,
        @NotNull List<String> roles,
        @NotNull String fullName) {
}
