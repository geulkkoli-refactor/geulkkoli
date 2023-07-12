package com.geulkkoli.application.security;

import com.geulkkoli.application.security.util.RoleNameAttributeConverter;
import com.geulkkoli.domain.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by hoogokok on 2023/4/21
 * Role Entity
 * Role Entity는 Role Enum 타입을 DB에 저장하기 위한 엔티티 입니다.
 * Role Entity는 User Entity와 일대다 연관관계를 가집니다.
 * User Entity는 Role Entity와 다대일 연관관계를 가집니다.
 */
@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Convert(converter = RoleNameAttributeConverter.class)
    @Column(name = "role_number")
    private Role role;
    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();

    public RoleEntity() {
    }

    private RoleEntity(Role role, User user) {
        this.role = role;
        this.users.add(user);
    }

    public static RoleEntity of(Role role, User user ) {
        return new RoleEntity(role,user);
    }

    public Long getRoleId() {
        return roleId;
    }

    public Role getRole() {
        return role;
    }

    public Boolean isUser() {
        return role.equals(Role.USER);
    }

    public Boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }

    public Boolean isGuest() {
        return role.equals(Role.GUEST);
    }

    public Set<User> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleEntity)) return false;
        RoleEntity that = (RoleEntity) o;
        return Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }

    public String authority() {
        return role.getRoleName();
    }
}
