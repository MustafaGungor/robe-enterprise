package com.mebitech.robe.security.api.domain;

/**
 * Created by tayipdemircan on 27.03.2017.
 */
public interface RobePermission {

    String getPath();

    String getMethod();

    RobeRoleGroup getRoleGroup();

    PermissionType getPermissionType();

    RobeMenu getMenu();
}
