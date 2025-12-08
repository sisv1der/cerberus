package ru.yarigo.cerberus.auth.web.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull String username,
        @NotNull String password) {

}