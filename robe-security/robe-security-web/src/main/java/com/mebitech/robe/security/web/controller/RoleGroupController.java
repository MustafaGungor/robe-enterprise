package com.mebitech.robe.security.web.controller;

import com.mebitech.robe.security.api.domain.RobeRoleGroup;
import com.mebitech.robe.security.core.annotation.PermissionGroup;
import com.mebitech.robe.security.db.domain.RoleGroup;
import com.mebitech.robe.security.db.service.RoleGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by tayipdemircan on 28.03.2017.
 */
@RestController
@RequestMapping(value = "roles")
public class RoleGroupController {
    @Autowired
    private RoleGroupService roleGroupService;

    @PermissionGroup(name = "rolePermission")
    @RequestMapping(method = RequestMethod.GET)
    public List<? extends RobeRoleGroup> findAll() {
        return roleGroupService.findAll();
    }

    @PermissionGroup(name = "rolePermission")
    @RequestMapping(method = RequestMethod.GET, value = "{oid}")
    public RoleGroup find(@PathVariable("oid") String oid) {
        return roleGroupService.findOne(oid);
    }

    @PermissionGroup(name = "rolePermission")
    @RequestMapping(method = RequestMethod.POST)
    public RoleGroup create(@RequestBody RoleGroup role) {
        return roleGroupService.create(role);
    }

    @PermissionGroup(name = "rolePermission")
    @RequestMapping(value = "{oid}",  method = RequestMethod.PUT)
    public RoleGroup update(@PathVariable("oid") String oid,@RequestBody RoleGroup role) {
        return roleGroupService.update(role, oid);
    }

    @PermissionGroup(name = "rolePermission")
    @RequestMapping(method = RequestMethod.DELETE, value = "{oid}")
    public void delete(@PathVariable("oid") String id) {
        roleGroupService.delete(id);
    }
}
