package com.mebitech.robe.security.db.service;

import com.mebitech.robe.persistence.jpa.services.JpaService;
import com.mebitech.robe.security.api.domain.RobeUser;
import com.mebitech.robe.security.api.model.SessionUser;
import com.mebitech.robe.security.db.repository.RobeUserRepository;
import com.mebitech.robe.security.api.service.RobeUserService;
import com.mebitech.robe.security.db.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
@Service
public class UserService extends JpaService<User, String> implements RobeUserService {

    RobeUserRepository repository;

    @Autowired
    public UserService(RobeUserRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Find user by username and match passwords(db password and request password)
     * @param username
     * @param password
     * @return
     */
    @Override
    public Optional<? extends RobeUser> getUserByUsernameAndPassword(String username, String password) {
        Optional<User> user = repository.findOneByUsername(username);
        if(user.isPresent()){
            User userObj = (User)user.get();
            if(userObj.getPassword().equals(password)){
                return (Optional<? extends RobeUser>)user;
            }
        }
        return null;
    }

    public SessionUser getSessionUser(){
        SessionUser user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            user = (SessionUser) auth.getPrincipal();
        }

        return user;
    }
}
