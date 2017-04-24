package com.mebitech.robe.security.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mebitech.robe.security.api.domain.RobeRoleGroup;
import com.mebitech.robe.security.api.domain.RobeUser;
import com.mebitech.robe.security.api.model.SessionUser;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
public class SessionUserImpl extends org.springframework.security.core.userdetails.User implements SessionUser {

    private RobeUser robeUser;

    public SessionUserImpl(RobeUser robeUser){
        super(robeUser.getUsername(),robeUser.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(robeUser.getRoleGroup() != null ? robeUser.getRoleGroup().getCode() : ""));
        this.robeUser = robeUser;
    }


    @JsonIgnore
    @Override
    public RobeUser getUser() {
        return robeUser;
    }

    @Override
    public String getId() {
        return robeUser.getUserId();
    }

    @Override
    public String getUsername() {
        return robeUser.getUsername();
    }

    @Override
    public List getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(robeUser.getRoleGroup() != null ? robeUser.getRoleGroup().getCode() : "");
    }

//    private static String getPermissionGroups(RobeUser user){
//        String groups = "";
//        for(RobeRoleGroup permission : user.getRoleGroup()){
//            if(groups.length() > 0)
//                groups += ",";
//            groups += permission.getCode();
//        }
//        return groups;
//    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    @JsonIgnore
    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return super.getPassword();
    }
}
