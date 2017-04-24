package com.mebitech.robe.security.api.service;

import com.mebitech.robe.security.api.domain.PermissionType;
import com.mebitech.robe.security.api.domain.RobePermission;
import com.mebitech.robe.security.api.domain.RobeRoleGroup;

import java.util.List;

/**
 * Created by tayipdemircan on 27.03.2017.
 */
public interface RobePermissionService {

    List<? extends RobePermission> findAll();

    List<? extends RobePermission> findByPermissionTypeAndRoleGroup(PermissionType type, RobeRoleGroup role);

    Integer deleteByRoleGroupAndPermissionType(RobeRoleGroup roleGroup,PermissionType permissionType);
}
