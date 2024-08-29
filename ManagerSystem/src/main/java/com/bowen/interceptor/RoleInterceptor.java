package com.bowen.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.Map;


public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //pass the role info into all urls
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic")) {
            String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> credentialMap = objectMapper.readValue(credentials, Map.class);
            if (!credentialMap.containsKey("userId") || !credentialMap.containsKey("role")
                    || !credentialMap.containsKey("accountName")) {
                throw new SecurityException("Invalid credentials format");
            }

            request.setAttribute("userId", credentialMap.get("userId"));
            request.setAttribute("role", credentialMap.get("role"));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return false;
        }
        return true;
    }
}
