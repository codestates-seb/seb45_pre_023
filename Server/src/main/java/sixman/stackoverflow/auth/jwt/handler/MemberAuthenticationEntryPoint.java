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

import static sixman.stackoverflow.auth.utils.AuthConstant.*;

@Slf4j
@Component
public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        BusinessException businessException = (BusinessException) request.getAttribute(BUSINESS_EXCEPTION);
        Exception exception = (Exception) request.getAttribute(EXCEPTION);

        if(exist(businessException)){
            setResponseHeaderFrom(request, response, businessException);
            AuthUtil.sendErrorResponse(response, businessException);
            return;
        }

        if(authException instanceof InsufficientAuthenticationException){
            AuthUtil.sendErrorResponse(response, new MemberAccessDeniedException());
            return;
        }

        if(exist(exception)){
            printLog(exception);
            AuthUtil.sendErrorResponse(response, new UnknownException(exception.getMessage()));
            return;
        }

        printLog(authException);
        AuthUtil.sendErrorResponse(response, new UnknownException(authException.getMessage()));
    }

    private boolean exist(Exception exception) {
        return exception != null;
    }

    private void printLog(Exception exception) {
        log.error("Unknown error {} happened: {}", exception.getClass().getName(), exception.getMessage());
        exception.printStackTrace();
    }

    private void setResponseHeaderFrom(HttpServletRequest request, HttpServletResponse response, BusinessException businessException) throws IOException {

        if(businessException instanceof JwtExpiredAuthException){
            response.setHeader(ALLOW, "POST");
            response.setHeader(LOCATION,
                    request.getScheme() + "://" + request.getServerName() +  AUTH_REFRESH_URL);
        }
    }

}
