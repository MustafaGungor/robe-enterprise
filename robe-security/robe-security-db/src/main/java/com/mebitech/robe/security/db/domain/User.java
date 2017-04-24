package com.mebitech.robe.security.db.domain;

import com.mebitech.robe.persistence.jpa.domain.BaseEntity;
import com.mebitech.robe.security.api.domain.RobeUser;

import javax.persistence.*;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity implements RobeUser {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne
    private RoleGroup roleGroup;

    @Override
    public String getUserId() {
        return getOid();
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public RoleGroup getRoleGroup() {
        return roleGroup;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setRoleGroup(RoleGroup roleGroup) {
        this.roleGroup = roleGroup;
    }
}
