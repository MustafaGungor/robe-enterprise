package com.mebitech.robe.security.db.domain;

import com.mebitech.robe.persistence.jpa.domain.BaseEntity;
import com.mebitech.robe.security.api.domain.PermissionType;
import com.mebitech.robe.security.api.domain.RobePermission;

import javax.persistence.*;

/**
 * Created by tayipdemircan on 27.03.2017.
 */
@Entity
public class Permission extends BaseEntity implements RobePermission {

    @Column(length = 100)
    private String path;

    @Column
    private String method;

    @ManyToOne
    private RoleGroup roleGroup;

    @Column
    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;

    @ManyToOne
    private Menu menu;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public RoleGroup getRoleGroup() {
        return roleGroup;
    }

    public void setRoleGroup(RoleGroup roleGroup) {
        this.roleGroup = roleGroup;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
