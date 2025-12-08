package ru.yarigo.cerberus.admin.dto;

import ru.yarigo.cerberus.users.profiles.model.Profile;
import ru.yarigo.cerberus.users.user.model.User;

public record UserRegistered(
    User user,
    Profile profile
) {}
