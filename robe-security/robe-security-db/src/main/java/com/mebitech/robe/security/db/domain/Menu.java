package com.mebitech.robe.security.db.domain;

import com.mebitech.robe.persistence.jpa.domain.BaseEntity;
import com.mebitech.robe.security.api.domain.RobeMenu;

import javax.persistence.*;
import java.util.List;

/**
 * Created by tayipdemircan on 29.03.2017.
 */
@Entity
public class Menu extends BaseEntity implements RobeMenu {
    @Column(length = 50, nullable = false)
    private String text;

    @Column(length = 100, nullable = false)
    private String path;

    @Column(name = "itemIndex")
    private int index;

    @ManyToOne
    @JoinColumn(name = "parent_oid")
    private Menu parent;

    @Column(length = 50)
    private String module;

    @Column(length = 30)
    private String icon;

    @Column(length = 30)
    private String value;

    @Transient
    private List<Menu> items;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Menu> getItems() {
        return items;
    }

    public void setItems(List<Menu> items) {
        this.items = items;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
