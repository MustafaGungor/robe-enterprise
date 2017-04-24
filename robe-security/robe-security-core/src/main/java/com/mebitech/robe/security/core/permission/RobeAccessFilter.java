package com.mebitech.robe.security.core.permission;


import com.mebitech.robe.security.api.domain.PermissionType;
import com.mebitech.robe.security.api.domain.RobePermission;
import com.mebitech.robe.security.api.domain.RobeRoleGroup;
import com.mebitech.robe.security.api.model.SessionUser;
import com.mebitech.robe.security.api.service.RobePermissionService;
import com.mebitech.robe.security.api.service.RobeRoleGroupService;
import com.mebitech.robe.security.core.annotation.PermissionGroup;
import com.mebitech.robe.security.core.cache.SecurityCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by tayipdemircan on 27.03.2017.
 */
public class RobeAccessFilter extends GenericFilterBean {

    /**
     *
     */
    @Autowired
    private RobePermissionService permissionService;

    /**
     *
     */
    @Autowired
    private RobeRoleGroupService roleGroupService;

    /**
     *
     */
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     *
     */
    List<? extends RobePermission> permissionList;

    /**
     *
     */
    private static String restPath;

    /**
     *
     */
    private static final AntPathMatcher mathcer = new AntPathMatcher();

    /**
     * Filter rest requests using rolegroup permissions
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        loadPermissionList();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getRequestURI().indexOf(getRestPath()) > -1 && request.getRequestURI().indexOf(getRestPath() + "login") < 0) {
            if (permissionList != null && !hasPermitted(request.getRequestURI(), request.getMethod())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Permission Denied");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     *
     */
    @Override
    public void destroy() {
        System.out.println("RobeAccessFilter destroy");
    }

    /**
     * loadpermissionlist for granted authority
     */
    private void loadPermissionList() {
        permissionList = (List<? extends RobePermission>) SecurityCache.get("permissionList#" + getAuthorityCode());
        if (permissionList == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof SessionUser) {
                SessionUser user = (SessionUser) authentication.getPrincipal();
                for (SimpleGrantedAuthority authority : (List<SimpleGrantedAuthority>) user.getAuthorities()) {
                    RobeRoleGroup roleGroup = getRoleGroup(authority.getAuthority());
                    if (roleGroup != null) {
                        if (permissionList == null)
                            permissionList = permissionService.findByPermissionTypeAndRoleGroup(PermissionType.REST, roleGroup);
                    }
                }
                SecurityCache.put("permissionList#"+getAuthorityCode(), permissionList);
            }
        }
    }

    /**
     *
     * @param url
     * @param method
     * @return
     */
    private boolean hasPermitted(String url, String method) {
        boolean ret = true;
        if (permissionList != null) {
            for (RobePermission permission : permissionList) {
                if (permission.getMethod() != null && !permission.getMethod().equals("")) {
                    if (mathcer.match(permission.getPath(),url) && permission.getMethod().equals(method)) { //url.indexOf(permission.getPath()) > -1 &&
                        return true;
                    }
                } else {
                    List<Map<String, String>> matchedPaths = getMatchedPaths(permission.getPath());
                    for (Map<String, String> matchedPath : matchedPaths) {
                        String mPath = matchedPath.get("path");
                        String mMethod = matchedPath.get("method");
                        if (mathcer.match(mPath,url) && method.equals(mMethod)) {
                            return true;
                        }
                    }
                }
                ret = false;
            }
        }
        return ret;
    }

    /**
     *
     * @param code
     * @return
     */
    private RobeRoleGroup getRoleGroup(String code) {
        List<? extends RobeRoleGroup> roleGroups = roleGroupService.findByCode(code);
        return roleGroups.size() > 0 ? roleGroups.get(0) : null;
    }

    /**
     *
     * @param permissionGroup
     * @return
     */
    private List<Map<String, String>> getMatchedPaths(String permissionGroup) {
        List<Map<String, String>> matchedPaths = new ArrayList<>();
        Set<RequestMappingInfo> requestMappingInfos = requestMappingHandlerMapping.getHandlerMethods().keySet();
        for (RequestMappingInfo info : requestMappingInfos) {
            Method rMethod = requestMappingHandlerMapping.getHandlerMethods().get(info).getMethod();
            String group = "";
            if (rMethod.getAnnotation(PermissionGroup.class) != null)
                group = rMethod.getAnnotation(PermissionGroup.class).name();
            if (group.equals(permissionGroup)) {
                String path = (String) info.getPatternsCondition().getPatterns().toArray()[0];
                RequestMethod method = null;
                if (info.getMethodsCondition().getMethods().toArray().length > 0)
                    method = (RequestMethod) info.getMethodsCondition().getMethods().toArray()[0];

                if (method != null && !path.equals("")) {
                    Map<String, String> pathMap = new HashMap<>();
                    pathMap.put("path", (getRestPath() + path).replace("//","/"));
                    pathMap.put("method", method.name());
                    matchedPaths.add(pathMap);
                }
            }
        }
        return matchedPaths;
    }

    /**
     *
     * @return
     */
    public static String getRestPath() {
        return restPath;
    }

    /**
     *
     * @param restPath
     */
    public static void setRestPath(String restPath) {
        RobeAccessFilter.restPath = restPath;
    }

    /**
     *
     * @return authenticated user
     */
    private static SessionUser getSessionUser(){
        SessionUser user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SessionUser)
            user = (SessionUser) authentication.getPrincipal();

        return user;
    }

    /**
     *
     * @return authority code
     */
    private static String getAuthorityCode(){
        if(getSessionUser() != null) {
            SimpleGrantedAuthority authority = (SimpleGrantedAuthority) getSessionUser().getAuthorities().get(0);
            return authority.getAuthority();
        }
        return "";
    }

}
