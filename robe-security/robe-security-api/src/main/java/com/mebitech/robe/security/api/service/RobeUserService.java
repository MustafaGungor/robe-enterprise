package com.mebitech.robe.security.api.service;

import com.mebitech.robe.security.api.domain.RobeUser;

import java.util.Optional;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
public interface RobeUserService {
    /**
     * Match login username pasword data and db/ldap username password data
     * @param username
     * @param password
     * @return
     */
    Optional<? extends RobeUser> getUserByUsernameAndPassword(String username, String password);
}
