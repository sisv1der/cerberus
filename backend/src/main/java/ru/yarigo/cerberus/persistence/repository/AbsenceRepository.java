package ru.yarigo.cerberus.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yarigo.cerberus.persistence.model.Absence;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
}
