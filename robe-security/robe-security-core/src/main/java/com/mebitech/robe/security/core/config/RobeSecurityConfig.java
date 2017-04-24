package com.mebitech.robe.security.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mebitech.robe.security.core.entryPoint.RobeAuthenticationEntryPointImpl;
import com.mebitech.robe.security.core.filter.RobeUsernamePasswordAuthenticationFilter;
import com.mebitech.robe.security.core.handler.RobeAuthenticationFailureHandler;
import com.mebitech.robe.security.core.handler.RobeAuthenticationSuccessHandler;
import com.mebitech.robe.security.core.handler.RobeLogoutSuccessHandler;
import com.mebitech.robe.security.core.permission.RobeAccessFilter;
import com.mebitech.robe.security.core.provider.RobeAuthenticationProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.util.ArrayList;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class RobeSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RobeAuthenticationProviderImpl autheticationProvider;

    @Autowired
    private RobeAuthenticationEntryPointImpl authenticationEntryPoint;

    private static String restPath;

    private static String[] permittedPaths;

    /**
     * HttpSecurity configuration
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(permittedPaths).permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage(getRestPath() + "/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl(getRestPath() + "/logout")
                .logoutSuccessHandler(new RobeLogoutSuccessHandler())
                .deleteCookies("remember-me", "JSESSIONID")
                .permitAll()
                .and()
                .rememberMe()
                .and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilterBefore(getRobeUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(getRobeAccessFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * AuthenticationManager configuration
     *
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(autheticationProvider);
    }

    /**
     * Create CustomUsernamePaswordAuthenticationFilter
     *
     * @return
     * @throws Exception
     */
    public RobeUsernamePasswordAuthenticationFilter getRobeUsernamePasswordAuthenticationFilter() throws Exception {
        RobeUsernamePasswordAuthenticationFilter filter = new RobeUsernamePasswordAuthenticationFilter(new RobeAuthenticationSuccessHandler(), new RobeAuthenticationFailureHandler(), new ObjectMapper());
        filter.setAuthenticationManager(authenticationManager());
        filter.setSessionAuthenticationStrategy(getSessionAuthenticationStrategy());
        return filter;
    }

    /**
     * Create SessionAuthenticationStrategy
     *
     * @return
     */
    public SessionAuthenticationStrategy getSessionAuthenticationStrategy() {
        SessionRegistryImpl sessionRegistry = new SessionRegistryImpl();
        RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy = new RegisterSessionAuthenticationStrategy(sessionRegistry);
        ChangeSessionIdAuthenticationStrategy strategy = new ChangeSessionIdAuthenticationStrategy();
        strategy.setApplicationEventPublisher(getApplicationContext());
        ArrayList list = new ArrayList();
        list.add(strategy);
        list.add(registerSessionAuthenticationStrategy);
        return new CompositeSessionAuthenticationStrategy(list);
    }

    @Bean
    public RobeAccessFilter getRobeAccessFilter(){
        return new RobeAccessFilter();
    }

    public static String getRestPath() {
        return restPath;
    }

    public static void setRestPath(String restPath) {
        RobeSecurityConfig.restPath = restPath;
    }

    public static String[] getPermittedPaths() {
        return permittedPaths;
    }

    public static void setPermittedPaths(String[] permittedPaths) {
        RobeSecurityConfig.permittedPaths = permittedPaths;
    }
}
