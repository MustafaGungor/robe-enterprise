package com.mebitech.robe.security.api.domain;

import java.util.Set;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
public interface RobeUser {
    /**
     * @return
     */
    String getUserId();

    /**
     * @return
     */
    String getUsername();


    /**
     * @return
     */
    String getPassword();

    /**
     * @return
     */
    boolean isActive();

    /**
     * @return
     */
    RobeRoleGroup getRoleGroup();
}
