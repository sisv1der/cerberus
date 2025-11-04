package ru.yarigo.cerberus.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yarigo.cerberus.persistence.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
