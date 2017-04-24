package com.mebitech.robe.security.db.service;

import com.mebitech.robe.persistence.jpa.services.JpaService;
import com.mebitech.robe.security.api.domain.PermissionType;
import com.mebitech.robe.security.api.model.SessionUser;
import com.mebitech.robe.security.db.domain.Menu;
import com.mebitech.robe.security.db.domain.Permission;
import com.mebitech.robe.security.db.domain.RoleGroup;
import com.mebitech.robe.security.db.domain.model.MenuModel;
import com.mebitech.robe.security.db.repository.RobeMenuRepository;
import com.mebitech.robe.security.db.repository.RobePermissionRepository;
import com.mebitech.robe.security.db.repository.RobeRoleGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tayipdemircan on 29.03.2017.
 */
@Service
public class MenuService extends JpaService<Menu, String> {

    private List<RoleGroup> authorities;

    private Set<Permission> permissions;

    private RobeMenuRepository robeMenuRepository;

    @Autowired
    private RobePermissionRepository robePermissionRepository;

    @Autowired
    private RobeRoleGroupRepository robeRoleGroupRepository;

    @Autowired
    public MenuService(RobeMenuRepository repository) {
        super(repository);
        this.robeMenuRepository = repository;
    }

    public List<MenuModel> getMenuList() {
        return getMenuList(false);
    }

    public List<MenuModel> getMenuList(boolean all) {
        return getMenuList(all, null);
    }

    public List<Menu> findParents(){
        return robeMenuRepository.findByParent_oid(null);
    }

    public List<MenuModel> getMenuList(boolean all, RoleGroup roleGroup) {
        authorities = null;
        permissions = null;
        List<Menu> parents = robeMenuRepository.findByParent_oid(null);
        for (Menu menu : parents)
            loadSubMenus(menu);

        if(!all)
            parents = generatePermittedMenus(parents, roleGroup);

        List<MenuModel> retList = convertToModelClass(parents);
        return retList != null ? retList : new ArrayList<>();
    }


    private void loadSubMenus(Menu parentMenu) {
        List<Menu> subMenus = robeMenuRepository.findByParent_oid(parentMenu.getOid());
        if (subMenus != null && subMenus.size() > 0) {
            for (Menu subMenu : subMenus) {
                if (parentMenu.getItems() == null)
                    parentMenu.setItems(new ArrayList<Menu>());
                parentMenu.getItems().add(subMenu);
                loadSubMenus(subMenu);
            }
        }
    }

    private List<MenuModel> convertToModelClass(List<Menu> menuList) {
        List<MenuModel> retList = null;
        for (Menu menu : menuList) {
            if (retList == null)
                retList = new ArrayList<>();
            List<MenuModel> itemList = null;
            if (menu.getItems() != null && menu.getItems().size() > 0)
                itemList = convertToModelClass(menu.getItems());

            MenuModel model = new MenuModel(menu);
            model.setItems(itemList);
            retList.add(model);
        }
        return retList;
    }


    private List<RoleGroup> getAuthorities() {
        if(authorities == null){
            authorities = new ArrayList<>();
            SessionUser user = (SessionUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user != null) {
                for (GrantedAuthority authority : (List<GrantedAuthority>) user.getAuthorities()) {
                    List<RoleGroup> roleGroupList = robeRoleGroupRepository.findByCode(authority.getAuthority());
                    if (roleGroupList != null && roleGroupList.size() > 0)
                        authorities.add(roleGroupList.get(0));
                }
            }
        }
        return authorities;
    }

    private Set<Permission> getPermittedMenus(RoleGroup roleGroup) {
        List<RoleGroup> roleGroups = new ArrayList<>();
        roleGroups.add(roleGroup);
        return getPermittedMenus(roleGroups);
    }

    private Set<Permission> getPermittedMenus(List<RoleGroup> roleGroups) {
        if(permissions == null) {
            permissions = new HashSet<>();
            for (RoleGroup roleGroup : roleGroups) {
                List<Permission> permissionList = robePermissionRepository.findByPermissionTypeAndRoleGroup(PermissionType.MENU, roleGroup);
                permissions.addAll(permissionList);
            }
        }
        return permissions;
    }

    private List<Menu> generatePermittedMenus(List<Menu> menus, RoleGroup roleGroup) {
        List<Menu> retList = new ArrayList<>();
        Set<Permission> permissions = null;
        if(roleGroup != null)
                permissions = getPermittedMenus(roleGroup);
        else
            permissions = getPermittedMenus(getAuthorities());

        for (Menu menu : menus) {
            boolean find = false;
            for (Permission permission : permissions) {
                if (permission.getMenu().getOid().equals(menu.getOid())) {
                    find = true;
                    retList.add(menu);
                    break;
                }
            }
            if (!find && menu.getItems() != null && menu.getItems().size() > 0) {
                List<Menu> subList = generatePermittedMenus(menu.getItems(), roleGroup);
                if(subList != null && subList.size() > 0) {
                    menu.setItems(subList);
                    retList.add(menu);
                }
            }
        }
        return retList;
    }

}
