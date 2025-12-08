package ru.yarigo.cerberus.users.user.web.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.yarigo.cerberus.users.profiles.model.Profile;
import ru.yarigo.cerberus.users.roles.model.Role;
import ru.yarigo.cerberus.admin.web.dto.RegisterResponse;
import ru.yarigo.cerberus.users.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = Role.class)
public interface UserMapper {

    @Mappings({
            @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(Role::getName).toList())"),
            @Mapping(target = "id", source = "user.id"),
            @Mapping(target = "username", source = "user.username"),
            @Mapping(target = "email", source = "user.email"),
            @Mapping(target = "fullName", source = "profile.fullName")
    })
    RegisterResponse userAndProfileToRegisterResponse(User user, Profile profile);

    @Mappings({
            @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(Role::getName).toList())"),
            @Mapping(target = "id", source = "user.id"),
            @Mapping(target = "username", source = "user.username"),
            @Mapping(target = "email", source = "user.email"),
            @Mapping(target = "fullName", source = "profile.fullName"),
            @Mapping(target = "isActive", source = "user.active"),
            @Mapping(target = "createdAt", source = "user.createdAt")
    })
    UserInfo userAndProfileToUserInfo(User user, Profile profile);
}

