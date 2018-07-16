package com.bytter.scf.security.handler;

import com.bytter.scf.core.utils.CookieUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.ForwardLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class CustomLogoutSuccessHandler extends ForwardLogoutSuccessHandler implements LogoutSuccessHandler {
    /**
     * Construct a new {@link ForwardLogoutSuccessHandler} with the given target URL.
     *
     * @param targetUrl the target URL
     */
    public CustomLogoutSuccessHandler(String targetUrl) {
        super(targetUrl);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CookieUtils.removeCookie(request, response, "BTACTK");
        super.onLogoutSuccess(request, response, authentication);
    }
}
