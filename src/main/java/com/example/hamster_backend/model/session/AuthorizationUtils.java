package com.example.hamster_backend.model.session;

import com.example.hamster_backend.model.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class AuthorizationUtils {
    public static Long getAuthUserId() {
        if (!authComplete()) {
            return -1L;
        }

        User u = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return (long) u.getId();
    }

    public static boolean authComplete() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    public static User getAuthUser() {
        if (!authComplete()) {
            return null;
        }

        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    public static String getUserName(){
        if (!authComplete()) {
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


}
