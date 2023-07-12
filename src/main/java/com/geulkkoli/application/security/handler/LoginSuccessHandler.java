package com.geulkkoli.application.security.handler;

import com.geulkkoli.application.security.LoginProcessException;
import com.geulkkoli.application.security.Role;
import com.geulkkoli.application.user.CustomAuthenticationPrinciple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomAuthenticationPrinciple principal = (CustomAuthenticationPrinciple) authentication.getPrincipal();
        principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).findAny().ifPresent(role -> {
            log.info("roleName2 : {}", role);
            if (Role.isGuest(role)) {
                try {
                    response.sendRedirect("/social/oauth2/signup");
                } catch (IOException e) {
                    throw getLoginProcessException(e);
                }
            }
            if (Role.isUser(role)) {
                try {
                    response.sendRedirect("/");
                } catch (IOException e) {
                    throw getLoginProcessException(e);
                }
            }
            if (Role.isAdmin(role)) {
                try {
                    log.info("admin : {}",  role);
                    response.sendRedirect("/admin");
                } catch (IOException e) {
                    throw getLoginProcessException(e);
                }
            }
        });

    }

    private LoginProcessException getLoginProcessException(IOException e) {
        return new LoginProcessException(e.getMessage());
    }
}
