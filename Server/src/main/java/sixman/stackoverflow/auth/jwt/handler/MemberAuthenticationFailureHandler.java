package sixman.stackoverflow.auth.jwt.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import sixman.stackoverflow.auth.jwt.utils.AuthUtil;
import sixman.stackoverflow.global.exception.businessexception.commonexception.UnknownException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberBadCredentialsException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if(exception instanceof BadCredentialsException){
            AuthUtil.sendErrorResponse(response, new MemberBadCredentialsException());
            return;
        }
        log.error("# Authentication failed with unknown reason : {}", exception.getMessage());
        AuthUtil.sendErrorResponse(response, new UnknownException());
    }
}