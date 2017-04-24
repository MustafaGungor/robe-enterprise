package com.mebitech.robe.security.db.domain.model;

import com.mebitech.robe.security.db.domain.Menu;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tayipdemircan on 29.03.2017.
 */
public class MenuModel implements Serializable {

    public MenuModel(Menu menu) {
        this.oid = menu.getOid();
        this.text = menu.getText();
        this.path = menu.getPath();
        this.icon = menu.getIcon();
        this.module = menu.getModule();
        this.items = menu.getItems();
        this.value = menu.getValue();
    }

    private String oid;

    private String text;

    private String value;

    private String path;

    private String module;

    private String icon;

    private List items;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getIcon() {
        return icon;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}
