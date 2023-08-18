package sixman.stackoverflow.auth.jwt.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import sixman.stackoverflow.auth.utils.AuthUtil;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;
import sixman.stackoverflow.global.exception.businessexception.authexception.JwtExpiredAuthException;
import sixman.stackoverflow.global.exception.businessexception.commonexception.UnknownException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        BusinessException businessException = (BusinessException) request.getAttribute("businessException");
        if(businessException != null){
            AuthUtil.sendErrorResponse(response, businessException);

            if(businessException instanceof JwtExpiredAuthException){
                response.setHeader("Allow", "POST");
                response.setHeader("Location",
                        request.getScheme() + "://" + request.getServerName() +  "/auth/refresh");
            }
            return;
        }

        if(authException instanceof InsufficientAuthenticationException){
            AuthUtil.sendErrorResponse(response, new MemberAccessDeniedException());
            return;
        }


        Exception exception = (Exception) request.getAttribute("exception");
        logExceptionMessage(authException, exception);

        authException.printStackTrace();
        AuthUtil.sendErrorResponse(response, new UnknownException(exception.getMessage()));
    }

    private void logExceptionMessage(AuthenticationException authException, Exception exception) {
        String message = exception != null ? exception.getMessage() : authException.getMessage();
        log.error("Unauthorized error happened: {}", message);
    }

}
