package com.mebitech.robe.security.core.provider;

import com.mebitech.robe.security.api.domain.RobeUser;
import com.mebitech.robe.security.api.model.SessionUser;
import com.mebitech.robe.security.api.provider.RobeAuthenticationProvider;
import com.mebitech.robe.security.api.service.RobeUserService;
import com.mebitech.robe.security.core.model.SessionUserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
@Service
public class RobeAuthenticationProviderImpl implements RobeAuthenticationProvider {

    @Autowired
    private RobeUserService robeUserService;

    /**
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    /**
     * Custom authentication
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication.getPrincipal() instanceof SessionUser) // if user already logged in
            return authentication;
        String userName = (String)authentication.getPrincipal(); // username
        String password = (String)authentication.getCredentials(); // password
        Optional<? extends RobeUser> user = robeUserService.getUserByUsernameAndPassword(userName,password); // match user using username and password
        if(user != null && user.isPresent()) {// user is present
            SessionUserImpl currentUser = new SessionUserImpl(user.get()); // create current user data using user entity
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(currentUser, password,currentUser.getAuthorities()); // create token for logged in user
            return token;
        } else{ // user is not present
            throw new InternalAuthenticationServiceException("Invalid Username or Password");
        }
    }
}
