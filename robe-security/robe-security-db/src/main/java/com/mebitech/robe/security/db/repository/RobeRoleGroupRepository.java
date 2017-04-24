package com.mebitech.robe.security.db.repository;

import com.mebitech.robe.security.db.domain.RoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by tayipdemircan on 28.03.2017.
 */
public interface RobeRoleGroupRepository extends JpaRepository<RoleGroup, String> {
    List<RoleGroup> findByCode(String code);
}
