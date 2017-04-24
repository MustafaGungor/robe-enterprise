package com.mebitech.robe.security.core;

import com.mebitech.robe.core.bundle.Configuration;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
public class RobeSecurityConfiguration implements Configuration {
    private String path;

    private String authType;

    private String[] permittedPaths;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String[] getPermittedPaths() {
        return permittedPaths;
    }

    public void setPermittedPaths(String[] permittedPaths) {
        this.permittedPaths = permittedPaths;
    }
}
