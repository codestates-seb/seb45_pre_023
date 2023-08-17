package sixman.stackoverflow.auth.jwt.handler;

import lombok.extern.slf4j.Slf4j;
import sixman.stackoverflow.auth.utils.AuthUtil;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;
import sixman.stackoverflow.global.exception.businessexception.authexception.AuthException;
import sixman.stackoverflow.global.exception.businessexception.commonexception.UnknownException;
import sixman.stackoverflow.global.exception.businessexception.requestexception.RequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class MemberRefreshFailureHandler {

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException, ServletException {

        if(exception instanceof AuthException){

            response.setHeader("Allow", "POST");
            response.setHeader("Location", request.getScheme() + "://" + request.getServerName() +  "/auth/login");
            AuthUtil.sendErrorResponse(response, (AuthException) exception);
            return;
        }

        if(exception instanceof RequestException){
            response.setHeader("Allow", "POST");
            response.setHeader("Location", request.getScheme() + "://" + request.getServerName() +  "/auth/refresh");
            AuthUtil.sendErrorResponse(response, (RequestException) exception);
            return;
        }

        if(exception instanceof BusinessException){
            AuthUtil.sendErrorResponse(response, (BusinessException) exception);
            return;
        }

        log.error("# Authentication failed with unknown reason : {}", exception.getMessage());
        AuthUtil.sendErrorResponse(response, new UnknownException());
    }
}
