package org.openpaas.paasta.marketplace.web.seller.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.openpaas.paasta.marketplace.api.domain.User;

@Slf4j
public class SecurityUtils {

    public static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("authentication={}", authentication);

        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        log.debug("principal={}", principal);

        if (principal == null) {
            return null;
        }

        if (principal instanceof User) {
            return (User) principal;
        }

        return null;
    }

}
