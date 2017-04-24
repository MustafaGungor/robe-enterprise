package com.mebitech.robe.security.db.repository;

import com.mebitech.robe.security.db.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by tayipdemircan on 29.03.2017.
 */
public interface RobeMenuRepository extends JpaRepository<Menu, String> {
    List<Menu> findByParent_oid(String parentOid);
}
