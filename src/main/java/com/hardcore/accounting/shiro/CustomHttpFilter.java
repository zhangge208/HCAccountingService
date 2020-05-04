package com.hardcore.accounting.shiro;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class CustomHttpFilter extends PermissionsAuthorizationFilter {

    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
        String currentPath = getPathWithinApplication(request);
        log.debug("Path in CustomHttpFilter : {}, currentPath: {}", path, currentPath);
        val array = path.split("::");
        val url = array[0];
        boolean isHttpMethodMatched = true;
        if (array.length > 1) {
            val methodInRequest = ((HttpServletRequest) request).getMethod().toUpperCase();
            val method = array[1];
            isHttpMethodMatched = method.equals(methodInRequest);
        }
        return pathsMatch(url, currentPath) && isHttpMethodMatched;
    }
}
