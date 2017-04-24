package com.mebitech.robe.security.core.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

/**
 * Created by tayipdemircan on 28.03.2017.
 */
public class SecurityCache {
    static CacheManager manager;

    public static void init(){
        Configuration configuration = new Configuration().defaultCache(new CacheConfiguration("defaultCache", 1000))
                .cache(new CacheConfiguration("robe-security-cache", 1000).timeToIdleSeconds(0).timeToLiveSeconds(0));
        manager = CacheManager.create(configuration);
    }

    public static void destroy() {
        manager.shutdown();
    }

    public static Object get(Object key) {
        if (null == manager.getCache("robe-security-cache").get(key)) {
            return null;
        }
        Element elt = manager.getCache("robe-security-cache").get(key);
        return elt.getObjectValue();
    }

    public static void put(Object key, Object value) {
        manager.getCache("robe-security-cache").put(new Element(key, value));

    }
}
