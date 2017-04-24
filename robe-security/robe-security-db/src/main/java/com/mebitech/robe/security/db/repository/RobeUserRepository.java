package com.mebitech.robe.security.db.repository;

import com.mebitech.robe.security.db.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
public interface RobeUserRepository extends JpaRepository<User, String> {
    Optional<User> findOneByUsername(String username);
}
