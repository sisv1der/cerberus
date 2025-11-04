package ru.yarigo.cerberus.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profiles_id_seq")
    @SequenceGenerator(name = "profiles_id_seq", sequenceName = "profiles_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(name = "full_name", nullable = false)
    @ToString.Include
    private String fullName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "profile_groups",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @ToString.Include
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "absence_profiles",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "absence_id")
    )
    private Set<Absence> absences = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public void setUser(User user) {
        this.user = user;
        this.user.setProfile(this);
    }
}
