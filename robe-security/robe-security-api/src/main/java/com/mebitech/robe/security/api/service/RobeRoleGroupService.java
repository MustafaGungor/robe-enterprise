package com.mebitech.robe.security.api.service;

import com.mebitech.robe.security.api.domain.RobeRoleGroup;

import java.util.List;

/**
 * Created by tayipdemircan on 5.04.2017.
 */
public interface RobeRoleGroupService {
    List<? extends RobeRoleGroup> findByCode(String code);
}
