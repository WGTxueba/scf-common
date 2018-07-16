package com.bytter.scf.security.handler;

import com.bytter.scf.core.utils.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    RequestCache requestCache = new HttpSessionRequestCache();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String redirectUrl = "";
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null && StringUtils.isNotEmpty(savedRequest.getRedirectUrl())) {
            redirectUrl = savedRequest.getRedirectUrl();
        }

        boolean isAjax = "XMLHttpRequest".equals(request
                .getHeader("X-Requested-With")) || "apiLogin".equals(request
                .getHeader("api-login"));

        if (isAjax) {
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            try {
//                logger.info("登录成功");
//
//                String header = request.getHeader("Authorization");
//
//                if (header == null || !header.startsWith("Basic ")) {
//                    throw new UnapprovedClientAuthenticationException("请求头中无client信息");
//                }
//
//                String[] tokens = extractAndDecodeHeader(header);
//                assert tokens.length == 2;

//                String clientId = tokens[0];
//                String clientSecret = tokens[1];

                String clientId = "client_1";
                String clientSecret = new BCryptPasswordEncoder().encode("123456");

                ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

                if (clientDetails == null) {
                    throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
                }
//                else if (!org.apache.commons.lang.StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
//                    throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientId);
//                }

                TokenRequest tokenRequest = new TokenRequest(Collections.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");

                OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

                OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

                OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

                CookieUtils.setCookie(response,"BTACTK",token.getValue(),0);

                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(HttpEntity.EMPTY));
            } catch (Exception ex) {
                if (logger.isErrorEnabled()) {
                    logger.error("Could not write JSON:", ex);
                }
                throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
            }
        } else {
            //Call the parent method to manage the successful authentication
            //setDefaultTargetUrl("/");
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }


    private String[] extractAndDecodeHeader(String header) throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, "UTF-8");

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

}
