package ru.yarigo.cerberus.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yarigo.cerberus.persistence.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
