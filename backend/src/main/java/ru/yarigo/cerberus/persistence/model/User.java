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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    @ToString.Include
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    @ToString.Include
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "created_at", nullable = false)
    @ToString.Include
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Include
    private Set<Role> roles = new HashSet<>();

    public void setRoles(Set<Role> roles) {
        for (Role role : roles) {
            addRole(role);
        }
    }

    private void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private Profile profile;

    public static class Builder {
        private String username;
        private String email;
        private String passwordHash;
        private final LocalDateTime createdAt = LocalDateTime.now();
        private Set<Role> roles;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public Builder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public User build() {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setRoles(roles);
            user.setPasswordHash(passwordHash);
            user.setCreatedAt(createdAt);

            for (Role role : roles) {
                role.getUsers().add(user);
            }

            return user;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
