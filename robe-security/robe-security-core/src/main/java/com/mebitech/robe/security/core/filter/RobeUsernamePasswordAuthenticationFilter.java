package com.mebitech.robe.security.core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mebitech.robe.security.core.model.RobeCredentials;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

/**
 * Created by tayipdemircan on 13.03.2017.
 */
public class RobeUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper mapper;

    /**
     * LOGIN_URL for filtering login request
     */
    private static String LOGIN_URL = "";///rest/login

    /**
     * REQUEST_METHOD for filtering login request method
     */
    private static final String REQUEST_METHOD = "POST";

    /**
     * @param successHandler
     * @param failureHandler
     * @param mapper
     */
    public RobeUsernamePasswordAuthenticationFilter(AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, ObjectMapper mapper) {//AuthenticationManager authenticationManager
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(LOGIN_URL, REQUEST_METHOD));
        this.mapper = mapper;
        this.setAuthenticationSuccessHandler(successHandler);
        this.setAuthenticationFailureHandler(failureHandler);
    }

    /**
     * Custom authentication using JSON data
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        RobeCredentials credentials = getRequestData(httpServletRequest);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        this.setDetails(httpServletRequest, token);
        return this.getAuthenticationManager().authenticate(token);
    }

    /**
     * Parse JSON data
     *
     * @param request
     * @return
     * @throws Exception
     */
    private RobeCredentials getRequestData(HttpServletRequest request) {
        StringBuffer jb = new StringBuffer();
        String line = null;
        String requestData = "";
        RobeCredentials credentials = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
            requestData = jb.toString();
            credentials = mapper.readValue(requestData, RobeCredentials.class);
        } catch (Exception e) { /*report an error*/ }

        return credentials;
    }

    public static String getLoginUrl() {
        return LOGIN_URL;
    }

    public static void setLoginUrl(String loginUrl) {
        LOGIN_URL = loginUrl;
    }
}