package com.mebitech.robe.security.web.controller;

import com.mebitech.robe.security.api.domain.PermissionType;
import com.mebitech.robe.security.api.domain.RobePermission;
import com.mebitech.robe.security.api.model.SessionUser;
import com.mebitech.robe.security.core.annotation.PermissionGroup;
import com.mebitech.robe.security.core.cache.SecurityCache;
import com.mebitech.robe.security.db.domain.Menu;
import com.mebitech.robe.security.db.domain.Permission;
import com.mebitech.robe.security.db.domain.RoleGroup;
import com.mebitech.robe.security.db.domain.model.MenuModel;
import com.mebitech.robe.security.db.service.MenuService;
import com.mebitech.robe.security.db.service.PermissionService;
import com.mebitech.robe.security.db.service.RoleGroupService;
import com.mebitech.robe.security.web.controller.model.EndPointTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by tayipdemircan on 28.03.2017.
 */
@RestController
@RequestMapping(value = "permissions")
public class PermissionController {

    @Value("${robe.security.path:restPath}")
    private String restPath;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleGroupService roleGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.GET)
    public List<? extends RobePermission> findAll() {
        return permissionService.findAll();
    }

    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.GET, value = "{oid}")
    public Permission find(@PathVariable("oid") String oid) {
        return permissionService.findOne(oid);
    }

    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.POST)
    public Permission create(@RequestBody Permission user) {
        SecurityCache.put("permissionList", null);
        return permissionService.create(user);
    }

    @PermissionGroup(name = "permissionController")
    @RequestMapping(value = "{oid}", method = RequestMethod.PUT)
    public Permission update(@PathVariable("oid") String oid, @RequestBody Permission user) {
        SecurityCache.put("permissionList", null);
        return permissionService.update(user, oid);
    }

    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.DELETE, value = "{oid}")
    public void delete(@PathVariable("oid") String id) {
        SecurityCache.put("permissionList", null);
        permissionService.delete(id);
    }

    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.GET, value = "/endPoints")
    public List restList(HttpServletRequest request, HttpServletResponse response) {
        List<EndPointTreeModel> retList = (List<EndPointTreeModel>) SecurityCache.get("endPoints");
        if (retList == null) {
            retList = new ArrayList<>();
            Set<RequestMappingInfo> requestMappingInfos = requestMappingHandlerMapping.getHandlerMethods().keySet();
            for (RequestMappingInfo info : requestMappingInfos) {
                String group = "";
                String path = "";
                RequestMethod method = null;
                Method rMethod = requestMappingHandlerMapping.getHandlerMethods().get(info).getMethod();
                if (rMethod.getAnnotation(PermissionGroup.class) != null)
                    group = rMethod.getAnnotation(PermissionGroup.class).name();

                path = (String) info.getPatternsCondition().getPatterns().toArray()[0];

                path = restPath + path;

                if (info.getMethodsCondition().getMethods().toArray().length > 0)
                    method = (RequestMethod) info.getMethodsCondition().getMethods().toArray()[0];

                if (method != null) {
                    if (group.equals("")) {
                        EndPointTreeModel model = new EndPointTreeModel();
                        model.setText(method.name() + " " + path);
                        model.setCode(method.name() + "#" + path);
                        model.setChildren(new ArrayList<>());
                        retList.add(model);
                    } else {
                        configurePermissionGroup(retList, group, path, method.name());
                    }
                }
            }
            SecurityCache.put("endPoints", retList);
        }
        return retList;
    }

    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.GET, value = "/endPoints/{oid}")
    public List restListByRoleId(@PathVariable("oid") String oid, HttpServletRequest request, HttpServletResponse response) {
        RoleGroup role = roleGroupService.findOne(oid);
        List<? extends RobePermission> permissions = permissionService.findByPermissionTypeAndRoleGroup(PermissionType.REST, role);
        List<String> retList = new ArrayList<>();
        for (Permission permission : (List<Permission>) permissions) {
            String permissionStr = permission.getPath();
            if (permission.getMethod() != null && !permission.getMethod().equals("")) {
                permissionStr = permission.getMethod() + "#" + permissionStr;
            } else {
                retList.addAll(getSubCodes(permissionStr));
            }
            retList.add(permissionStr);
        }

        return retList;
    }

    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.POST, value = "/configurePermission/{oid}")
    public List configurePermission(@PathVariable("oid") String roleOid, @RequestBody ArrayList<String> model,
                                    HttpServletRequest request, HttpServletResponse response) {

        RoleGroup roleGroup = roleGroupService.findOne(roleOid);
        permissionService.deleteByRoleGroupAndPermissionType(roleGroup, PermissionType.REST);
        Permission permission;
        for (String code : model) {
            permission = new Permission();
            permission.setRoleGroup(roleGroup);
            permission.setPermissionType(PermissionType.REST);
            String[] selectedCode = code.split("#");
            if (selectedCode.length > 1) {
                permission.setMethod(selectedCode[0]);
                permission.setPath(selectedCode[1]);
            } else {
                permission.setPath(selectedCode[0]);
            }

            permissionService.create(permission);
        }

        SecurityCache.put("permissionList#" + roleGroup.getCode(), null);

        return model;
    }

    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.GET, value = "/menus")
    public List menuList(HttpServletRequest request, HttpServletResponse response) {
        List<EndPointTreeModel> retList = (List<EndPointTreeModel>)SecurityCache.get("permissionMenuList");
        if(retList == null){
            retList = converToPermissionModel(menuService.getMenuList(true));
            SecurityCache.put("permissionMenuList",retList);
        }
        return retList;
    }

    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.GET, value = "/menus/{oid}")
    public List menuListForRoleGroup(@PathVariable("oid") String oid, HttpServletRequest request, HttpServletResponse response) {
        RoleGroup role = roleGroupService.findOne(oid);
        List<MenuModel> menuList = menuService.getMenuList(false, role);
        List<String> retList = getAuthorizedMenus(menuList);
        return retList;
    }

    @PermissionGroup(name = "permissionController")
    @RequestMapping(method = RequestMethod.POST, value = "/configuresMenu/{oid}")
    public List menuListOid(@PathVariable("oid") String roleOid,@RequestBody ArrayList<String> model,
                            HttpServletRequest request,HttpServletResponse response){
        RoleGroup roleGroup = roleGroupService.findOne(roleOid);
        permissionService.deleteByRoleGroupAndPermissionType(roleGroup, PermissionType.MENU);
        Permission permission;
        for (String code : model) {
            permission = new Permission();
            permission.setRoleGroup(roleGroup);
            permission.setPermissionType(PermissionType.MENU);
            permission.setMenu(menuService.findOne(code));

            permissionService.create(permission);
        }

        //SecurityCache.put("permissionList#" + roleGroup.getCode(), null);

        return model;


    }

    private List<String> getAuthorizedMenus(List<MenuModel> menuList){
        List<String> retList = new ArrayList<>();
        for(MenuModel model : menuList){
            retList.add(model.getOid());
            if(model.getItems() != null && model.getItems().size() > 0)
                retList.addAll(getAuthorizedMenus((List<MenuModel>) model.getItems()));
        }
        return retList;
    }

    private List<EndPointTreeModel> converToPermissionModel(List<MenuModel> menuList){
        List<EndPointTreeModel> retList = new ArrayList<>();
        EndPointTreeModel model;
        for(MenuModel menuModel : menuList){
            model = new EndPointTreeModel();
            model.setText(menuModel.getText());
            model.setCode(menuModel.getOid());
            if(menuModel.getItems() != null){
                model.setChildren(converToPermissionModel ((List<MenuModel>) menuModel.getItems()));
            }
            else{
                model.setChildren(new ArrayList<>());
            }
            retList.add(model);
        }
        return retList;
    }

    private void configurePermissionGroup(List<EndPointTreeModel> modelList, String permissionGroup, String path, String method) {
        boolean find = false;
        EndPointTreeModel addedModel = new EndPointTreeModel();
        addedModel.setText(method + " " + path);
        addedModel.setCode(method + "#" + path);
        addedModel.setChildren(new ArrayList<>());

        for (EndPointTreeModel model : modelList) {
            if (model.getCode().equals(permissionGroup)) {
                find = true;
                if (model.getChildren() == null)
                    model.setChildren(new ArrayList<>());
                model.getChildren().add(addedModel);
                break;
            }
        }

        if (!find) {
            EndPointTreeModel groupModel = new EndPointTreeModel();
            groupModel.setText(permissionGroup);
            groupModel.setCode(permissionGroup);
            groupModel.setChildren(new ArrayList<>());
            groupModel.getChildren().add(addedModel);
            modelList.add(groupModel);
        }
    }

    private List<String> getSubCodes(String permissionGroup) {
        List<EndPointTreeModel> modelList = (List<EndPointTreeModel>) SecurityCache.get("endPoints");
        List<String> retList = new ArrayList<>();
        if (modelList != null) {
            for (EndPointTreeModel model : modelList) {
                if (model.getCode().equals(permissionGroup)) {
                    for (EndPointTreeModel subModel : model.getChildren()) {
                        retList.add(subModel.getCode());
                    }
                    break;
                }
            }
        }

        return retList;
    }

}
