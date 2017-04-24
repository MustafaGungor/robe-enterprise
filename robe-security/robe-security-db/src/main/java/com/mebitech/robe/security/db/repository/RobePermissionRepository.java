package com.mebitech.robe.security.db.repository;

import com.mebitech.robe.security.api.domain.PermissionType;
import com.mebitech.robe.security.db.domain.Permission;
import com.mebitech.robe.security.db.domain.RoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by tayipdemircan on 27.03.2017.
 */
public interface RobePermissionRepository extends JpaRepository<Permission, String> {
    List<Permission> findByPermissionTypeAndRoleGroup(PermissionType type, RoleGroup role);

    @Modifying
    @Transactional
    @Query(value="delete from Permission c where c.roleGroup = ?1 and c.permissionType = ?2")
    Integer deleteByRoleGroupAndPermissionType(RoleGroup roleGroup, PermissionType type);
}
