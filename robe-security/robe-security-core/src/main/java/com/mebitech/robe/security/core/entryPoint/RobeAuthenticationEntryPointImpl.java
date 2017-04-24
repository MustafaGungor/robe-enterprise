package com.mebitech.robe.security.core.entryPoint;

import com.mebitech.robe.security.api.entryPoint.RobeAuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tayipdemircan on 21.03.2017.
 */
@Component("authenticationEntryPoint")
public class RobeAuthenticationEntryPointImpl implements RobeAuthenticationEntryPoint {
    /**
     * Authentication endpoint
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
