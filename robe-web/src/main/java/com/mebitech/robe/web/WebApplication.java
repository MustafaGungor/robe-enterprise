package com.mebitech.robe.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by kamilbukum on 28/02/2017.
 */
@SpringBootApplication
public class WebApplication extends SpringBootServletInitializer implements ApplicationRunner {

    @Value("${robe.value:init}")
    private String initKey;

    private static ConfigurableApplicationContext context;

    public static ConfigurableApplicationContext run(Class<?> clazz, String[] args) {
        context = SpringApplication.run(new Class[]{clazz}, args);
        return context;
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass()).bannerMode(Banner.Mode.OFF);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        if (applicationArguments.containsOption(initKey)) {
            init(applicationArguments);
            System.exit(0);
        }
    }

    public void init(ApplicationArguments applicationArguments) {

    }
}
