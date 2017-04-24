package com.mebitech.robe.security.db.service;

import com.mebitech.robe.persistence.jpa.services.JpaService;
import com.mebitech.robe.security.api.domain.PermissionType;
import com.mebitech.robe.security.api.domain.RobePermission;
import com.mebitech.robe.security.api.domain.RobeRoleGroup;
import com.mebitech.robe.security.api.service.RobePermissionService;
import com.mebitech.robe.security.db.domain.Permission;
import com.mebitech.robe.security.db.domain.RoleGroup;
import com.mebitech.robe.security.db.repository.RobePermissionRepository;
import com.mebitech.robe.security.db.repository.RobeRoleGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tayipdemircan on 27.03.2017.
 */
@Service
public class PermissionService extends JpaService<Permission, String> implements RobePermissionService {

    private RobePermissionRepository permissionRepository;

    @Autowired
    private RobeRoleGroupRepository roleGroupRepository;

    @Autowired
    public PermissionService(RobePermissionRepository repository) {
        super(repository);
        this.permissionRepository = repository;
    }

    public List<? extends RobePermission> findByPermissionTypeAndRoleGroup(PermissionType type, RoleGroup role) {
        return permissionRepository.findByPermissionTypeAndRoleGroup(type, role);
    }

    @Override
    public List<? extends RobePermission> findByPermissionTypeAndRoleGroup(PermissionType type, RobeRoleGroup role) {
        List<RoleGroup> roleGroups = roleGroupRepository.findByCode(role.getCode());
        return findByPermissionTypeAndRoleGroup(type, roleGroups.get(0));
    }

    @Override
    public Integer deleteByRoleGroupAndPermissionType(RobeRoleGroup roleGroup, PermissionType type) {
        List<RoleGroup> roleGroups = roleGroupRepository.findByCode(roleGroup.getCode());
        return permissionRepository.deleteByRoleGroupAndPermissionType(roleGroups.get(0), type);
    }
}
