package com.mebitech.robe.security.web.controller;

import com.mebitech.robe.security.api.domain.RobeUser;
import com.mebitech.robe.security.core.annotation.PermissionGroup;
import com.mebitech.robe.security.db.domain.RoleGroup;
import com.mebitech.robe.security.db.domain.User;
import com.mebitech.robe.security.db.service.RoleGroupService;
import com.mebitech.robe.security.db.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

/**
 * Created by tayipdemircan on 9.03.2017.
 */
@RestController
@RequestMapping(value = "users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleGroupService roleGroupService;

    @PermissionGroup(name = "userPermission")
    @RequestMapping(method = RequestMethod.GET)
    public List<? extends RobeUser> findAll() {
        return userService.findAll();
    }

    @PermissionGroup(name = "userPermission")
    @RequestMapping(method = RequestMethod.GET, value = "{oid}")
    public User find(@PathVariable("oid") String oid) {
        return userService.findOne(oid);
    }

    @PermissionGroup(name = "userPermission")
    @RequestMapping(method = RequestMethod.POST)
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PermissionGroup(name = "userPermission")
    @RequestMapping(value = "{oid}",  method = RequestMethod.PUT)
    public User update(@PathVariable("oid") String oid,@RequestBody User user) {
        return userService.update(user, oid);
    }

    @PermissionGroup(name = "userPermission")
    @RequestMapping(method = RequestMethod.DELETE, value = "{oid}")
    public void delete(@PathVariable("oid") String id) {
        userService.delete(id);
    }
}
