package ru.yarigo.cerberus.web.dto;

import java.util.List;

public record RegisterResponse(Long id, String username, String email, List<String> roles, String fullName) {
}
