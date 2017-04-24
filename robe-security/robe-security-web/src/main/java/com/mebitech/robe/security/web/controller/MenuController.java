package com.mebitech.robe.security.web.controller;

import com.mebitech.robe.security.api.domain.RobeMenu;
import com.mebitech.robe.security.core.annotation.PermissionGroup;
import com.mebitech.robe.security.db.domain.Menu;
import com.mebitech.robe.security.db.domain.model.MenuModel;
import com.mebitech.robe.security.db.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by tayipdemircan on 29.03.2017.
 */
@RestController
@RequestMapping(value = "menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PermissionGroup(name = "menuPermission")
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<? extends RobeMenu> findAll() {
        return menuService.findAll();
    }

    @PermissionGroup(name = "menuPermission")
    @RequestMapping(method = RequestMethod.GET)
    public List<MenuModel> findAllSessionMenu() {
        return menuService.getMenuList();
    }

    @PermissionGroup(name = "menuPermission")
    @RequestMapping(method = RequestMethod.GET, value = "{oid}")
    public Menu find(@PathVariable("oid") String oid) {
        return menuService.findOne(oid);
    }

    @PermissionGroup(name = "menuPermission")
    @RequestMapping(method = RequestMethod.POST)
    public Menu create(@RequestBody Menu role) {
        return menuService.create(role);
    }

    @PermissionGroup(name = "menuPermission")
    @RequestMapping(value = "{oid}", method = RequestMethod.PUT)
    public Menu update(@PathVariable("oid") String oid, @RequestBody Menu role) {
        return menuService.update(role, oid);
    }

    @PermissionGroup(name = "menuPermission")
    @RequestMapping(method = RequestMethod.DELETE, value = "{oid}")
    public void delete(@PathVariable("oid") String id) {
        menuService.delete(id);
    }

    @PermissionGroup(name = "menuPermission")
    @RequestMapping(value = "parents", method = RequestMethod.GET)
    public List<? extends RobeMenu> getParents() {
        return menuService.findParents();
    }

}
