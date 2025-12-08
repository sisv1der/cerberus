package ru.yarigo.cerberus.users.roles.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yarigo.cerberus.users.roles.model.Role;
import ru.yarigo.cerberus.users.roles.model.RoleRepository;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Set<Role> findByNameIn(List<String> names) {
        return Set.copyOf(roleRepository.findByNameIn(names));
    }
}
