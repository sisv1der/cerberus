package ru.yarigo.cerberus.web.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserInfo(
        String username,
        String email,
        String fullName,
        LocalDateTime createdAt,
        boolean isActive,
        List<String> roles,
        Long id) {
}
