package ru.yarigo.cerberus.web.dto;

import java.util.List;

public record RegisterRequest(String username, String email, List<String> roles, String fullName) {
}
