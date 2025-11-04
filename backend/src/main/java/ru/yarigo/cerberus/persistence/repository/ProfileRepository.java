package ru.yarigo.cerberus.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yarigo.cerberus.persistence.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
