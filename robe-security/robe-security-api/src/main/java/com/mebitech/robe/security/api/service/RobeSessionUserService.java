package com.mebitech.robe.security.api.service;

import com.mebitech.robe.security.api.model.SessionUser;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
public interface RobeSessionUserService {

    /**
     * Matches session user authorities and rest authorities
     *
     * @param sessionUser
     * @param authorities
     * @return
     */
    boolean hasMultiAuthority(SessionUser sessionUser, String... authorities);

    /**
     * Matches session user authorities and rest authorities as reverse
     *
     * @param sessionUser
     * @param authorities
     * @return
     */
    boolean hasReverseAuthority(SessionUser sessionUser, String... authorities);
}
