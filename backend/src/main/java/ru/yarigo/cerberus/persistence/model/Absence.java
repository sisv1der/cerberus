package ru.yarigo.cerberus.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "absences")
public class Absence {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "absences_id_seq")
    @SequenceGenerator(name = "absences_id_seq", sequenceName = "absences_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(name = "reason", nullable = false)
    @ToString.Include
    private String reason;

    @Column(name = "note")
    @ToString.Include
    private String note;

    @Column(name = "recorded_at", nullable = false)
    @ToString.Include
    private LocalDateTime recordedAt;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "absences")
    private Set<Profile> profiles = new HashSet<>();
}
