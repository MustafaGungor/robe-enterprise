package com.mebitech.robe.security.core.service;

import com.mebitech.robe.security.api.model.SessionUser;
import com.mebitech.robe.security.api.service.RobeSessionUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tayipdemircan on 22.03.2017.
 */
@Service
public class SessionUserServiceImpl implements RobeSessionUserService {


    /**
     * @param sessionUser
     * @param authorities
     * @return
     */
    @Override
    public boolean hasMultiAuthority(SessionUser sessionUser, String... authorities) {
        return hasAuthority(sessionUser, authorities);
    }

    /**
     * @param sessionUser
     * @param authorities
     * @return
     */
    @Override
    public boolean hasReverseAuthority(SessionUser sessionUser, String... authorities) {
        return !hasAuthority(sessionUser, authorities);
    }

    private boolean hasAuthority(SessionUser user, String... groups) {
        ArrayList<String> groupsArr = new ArrayList(Arrays.asList(groups));
        for (GrantedAuthority authority : (List<GrantedAuthority>) user.getAuthorities())
            if (groupsArr.contains(authority.getAuthority()))
                return true;

        return false;
    }
}
