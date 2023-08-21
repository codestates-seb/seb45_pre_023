package sixman.stackoverflow.auth.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import sixman.stackoverflow.auth.jwt.service.CustomUserDetails;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;

import java.util.stream.Collectors;

public class SecurityUtil {

    public static boolean isLogin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return checkLoggedIn(authentication);
    }

    public static Long getCurrentId() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!checkLoggedIn(authentication)){
            return null;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        validate(authentication);

        return userDetails.getMemberId();
    }

    public static String getCurrentEmail() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!checkLoggedIn(authentication)){
            return null;
        }

        validate(authentication);

        return authentication.getName();
    }

    public static Authority getAuthority(){

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!checkLoggedIn(authentication)){
            return null;
        }

        validate(authentication);

        return Authority.valueOf(authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining()));
    }

    private static boolean checkLoggedIn(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return !(authentication.getPrincipal() instanceof String && "anonymousUser".equals(authentication.getPrincipal()));
    }

    private static void validate(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new MemberNotFoundException();
        }
    }
}
