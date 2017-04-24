package com.mebitech.robe.web.config;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.PropertySource;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.io.InputStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kamilbukum on 10/03/2017.
 */

public class ConfigurationFinder {
    private static final String APP_CONFIG_REGEX = "applicationConfig\\: \\[classpath\\:(.*)\\]";
    private static final Pattern APP_CONFIG_PATTERN = Pattern.compile(APP_CONFIG_REGEX);

    public static com.mebitech.robe.common.converter.PropertySource getApplicationConfig(StandardServletEnvironment environment){
        Iterator<PropertySource<?>> it = environment.getPropertySources().iterator();
        String sourcePath = null;
        while (it.hasNext()) {
            PropertySource<?> lSource = it.next();
            Matcher m = APP_CONFIG_PATTERN.matcher(lSource.getName());
            if(m.find()) {
                sourcePath = m.group(1);
                break;
            }
        }
        if(sourcePath != null) {
            InputStream is = ConfigurationFinder.class.getResourceAsStream(sourcePath);
            com.mebitech.robe.common.converter.PropertySource.Type type = com.mebitech.robe.common.converter.PropertySource.Type.valueOf(
                    FilenameUtils.getExtension(sourcePath)
            );
            return new com.mebitech.robe.common.converter.PropertySource(type, is);
        }
        return null;
    }
}
