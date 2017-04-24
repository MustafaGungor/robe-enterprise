package com.mebitech.robe.security.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mebitech.robe.core.bundle.Bundle;
import com.mebitech.robe.core.bundle.BundleContext;
import com.mebitech.robe.security.core.cache.SecurityCache;
import com.mebitech.robe.security.core.config.RobeSecurityConfig;
import com.mebitech.robe.security.core.filter.RobeUsernamePasswordAuthenticationFilter;
import com.mebitech.robe.security.core.permission.RobeAccessFilter;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
public class RobeSecurityBundle extends Bundle<RobeSecurityConfiguration, Object> {
    private static final TypeReference<RobeSecurityConfiguration> TYPE_REFERENCE = new TypeReference<RobeSecurityConfiguration>() {
    };

    @Override
    public String getPropertyName() {
        return "robe.security";
    }

    @Override
    public TypeReference<RobeSecurityConfiguration> getTypeReference() {
        return TYPE_REFERENCE;
    }

    @Override
    public void onStart(RobeSecurityConfiguration configuration, BundleContext<Object> context) {
        RobeSecurityConfig.setRestPath(configuration.getPath());
        RobeSecurityConfig.setPermittedPaths(configuration.getPermittedPaths());
        RobeUsernamePasswordAuthenticationFilter.setLoginUrl(configuration.getPath() + "/login");
        RobeAccessFilter.setRestPath(configuration.getPath() + "/");
        SecurityCache.init();
    }

    @Override
    public void onStop(BundleContext<Object> context) {
        SecurityCache.destroy();
        super.onStop(context);
    }
}
