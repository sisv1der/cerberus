package ru.yarigo.cerberus.users.roles.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.yarigo.cerberus.users.permissions.model.Permission;
import ru.yarigo.cerberus.users.user.model.User;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    @SequenceGenerator(name = "roles_id_seq", sequenceName = "roles_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @ToString.Include
    private String name;

    @Column(name = "description")
    @ToString.Include
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @ToString.Include
    private Set<Permission> permissions = new HashSet<>();

    public void setPermissions(Set<Permission> permissions) {
        for (Permission permission : permissions) {
            addPermission(permission);
        }
    }

    private void addPermission(Permission permission) {
        permissions.add(permission);
        permission.getRoles().add(this);
    }
}
