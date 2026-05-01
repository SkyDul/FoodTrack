package com.foodtrack.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Custom CookieSerializer to allow simultaneous login for different roles
 * by using different cookie names based on the request URI.
 */
public class MultiRoleCookieSerializer implements CookieSerializer {
    private final DefaultCookieSerializer adminSerializer = new DefaultCookieSerializer();
    private final DefaultCookieSerializer petaniSerializer = new DefaultCookieSerializer();
    private final DefaultCookieSerializer defaultSerializer = new DefaultCookieSerializer();

    public MultiRoleCookieSerializer() {
        adminSerializer.setCookieName("ADMIN_SESSION");
        adminSerializer.setCookiePath("/");
        petaniSerializer.setCookieName("PETANI_SESSION");
        petaniSerializer.setCookiePath("/");
        defaultSerializer.setCookieName("SESSION");
        defaultSerializer.setCookiePath("/");
    }

    private CookieSerializer getSerializer(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isEmpty()) {
            uri = uri.substring(contextPath.length());
        }

        // Use ADMIN_SESSION for admin paths
        if (uri.equals("/admin") || uri.startsWith("/admin/")) {
            return adminSerializer;
        }
        // Use PETANI_SESSION for petani paths
        if (uri.equals("/petani") || uri.startsWith("/petani/") || uri.startsWith("/login-petani")) {
            return petaniSerializer;
        }
        // Default session for others
        return defaultSerializer;
    }

    @Override
    public void writeCookieValue(CookieValue cookieValue) {
        getSerializer(cookieValue.getRequest()).writeCookieValue(cookieValue);
    }

    @Override
    public List<String> readCookieValues(HttpServletRequest request) {
        return getSerializer(request).readCookieValues(request);
    }
}
