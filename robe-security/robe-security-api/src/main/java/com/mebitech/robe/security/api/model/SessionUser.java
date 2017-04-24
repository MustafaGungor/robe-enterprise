package com.mebitech.robe.security.api.model;

import com.mebitech.robe.security.api.domain.RobeUser;

import java.util.List;
import java.util.Set;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
public interface SessionUser {
    /**
     *
     * @return session user
     */
    RobeUser getUser();

    /**
     *
     * @return id of session user
     */
    String getId();

    /**
     *
     * @return name of session user
     */
    String getUsername();

    /**
     *
     * @return authorities of session user
     */
    List getAuthorities();
}
