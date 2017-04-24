package com.mebitech.robe.security.db.domain;

import com.mebitech.robe.persistence.jpa.domain.BaseEntity;
import com.mebitech.robe.security.api.domain.RobeRoleGroup;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
@Entity
public class RoleGroup extends BaseEntity implements RobeRoleGroup {

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
