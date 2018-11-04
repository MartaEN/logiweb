package com.marta.logistika.security;

import com.marta.logistika.exception.unchecked.UncheckedServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

/**
 * Part of security configurations
 * After login redirects users to different starting urls based on their roles
 */
public class UrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Log logger = LogFactory.getLog(this.getClass());
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public UrlAuthenticationSuccessHandler() {
        super();
    }

    // API

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    // IMPL

    private void handle(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
        final String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(final Authentication authentication) {
        boolean isLogist = false;
        boolean isDriver = false;
        boolean hasNoRole = false;
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String userRole = grantedAuthority.getAuthority();
            switch (userRole) {
                case "ROLE_LOGIST":
                    isLogist = true;
                    break;
                case "ROLE_DRIVER":
                    isDriver = true;
                    break;
                case "ROLE_NONE":
                    hasNoRole = true;
                    break;
                default:
                    throw new UncheckedServiceException(String.format("User with unknown role %s is trying to log in", userRole));
            }
        }

        if (isLogist) {
            return "/orders";
        } else if (isDriver || hasNoRole) {
            return "/logiweb";
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Removes temporary authentication-related data which may have been stored in the session
     * during the authentication process.
     */
    private void clearAuthenticationAttributes(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    public void setRedirectStrategy(final RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

}