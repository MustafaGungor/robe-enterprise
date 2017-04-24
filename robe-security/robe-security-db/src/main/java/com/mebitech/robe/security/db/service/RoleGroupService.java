package com.mebitech.robe.security.db.service;

import com.mebitech.robe.persistence.jpa.services.JpaService;
import com.mebitech.robe.security.api.service.RobeRoleGroupService;
import com.mebitech.robe.security.db.domain.RoleGroup;
import com.mebitech.robe.security.db.repository.RobeRoleGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tayipdemircan on 28.03.2017.
 */
@Service
public class RoleGroupService extends JpaService<RoleGroup, String> implements RobeRoleGroupService {

    private RobeRoleGroupRepository robeRoleGroupRepository;

    @Autowired
    public RoleGroupService(RobeRoleGroupRepository repository) {
        super(repository);
        this.robeRoleGroupRepository = repository;
    }

    public List<RoleGroup> findByCode(String code){
        return robeRoleGroupRepository.findByCode(code);
    }
}
