package sixman.stackoverflow.auth.jwt.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import sixman.stackoverflow.auth.utils.AuthUtil;
import sixman.stackoverflow.global.exception.businessexception.commonexception.UnknownException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberBadCredentialsException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.requestexception.RequestNotAllowedException;


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
        if(exception instanceof InternalAuthenticationServiceException){
            AuthUtil.sendErrorResponse(response, new MemberBadCredentialsException());
            return;
        }
        if(exception instanceof AuthenticationServiceException){
            AuthUtil.sendErrorResponse(response, new RequestNotAllowedException("POST"));
            return;
        }
        log.error("# Authentication failed with unknown reason : {}", exception.getMessage());
        AuthUtil.sendErrorResponse(response, new UnknownException());
    }
}
