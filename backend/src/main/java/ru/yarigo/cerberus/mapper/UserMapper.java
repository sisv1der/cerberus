package ru.yarigo.cerberus.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.yarigo.cerberus.persistence.model.Profile;
import ru.yarigo.cerberus.persistence.model.Role;
import ru.yarigo.cerberus.persistence.model.User;
import ru.yarigo.cerberus.web.dto.RegisterResponse;
import ru.yarigo.cerberus.web.dto.UserInfo;

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
            @Mapping(target = "isActive", source = "user.isActive"),
            @Mapping(target = "createdAt", source = "user.createdAt")
    })
    UserInfo userAndProfileToUserInfo(User user, Profile profile);
}

