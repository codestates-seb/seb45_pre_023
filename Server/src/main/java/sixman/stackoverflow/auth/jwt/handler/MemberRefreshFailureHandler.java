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

import static sixman.stackoverflow.auth.utils.AuthConstant.*;


@Slf4j
public class MemberRefreshFailureHandler {

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {

        if(exception instanceof AuthException){

            response.setHeader(ALLOW, "POST");
            response.setHeader(LOCATION, request.getScheme() + "://" + request.getServerName() + AUTH_LOGIN_URL);
            AuthUtil.sendErrorResponse(response, (AuthException) exception);
            return;
        }

        if(exception instanceof RequestException){
            response.setHeader(ALLOW, "POST");
            response.setHeader(LOCATION, request.getScheme() + "://" + request.getServerName() +  AUTH_REFRESH_URL);
            AuthUtil.sendErrorResponse(response, (RequestException) exception);
            return;
        }

        if(exception instanceof BusinessException){
            AuthUtil.sendErrorResponse(response, (BusinessException) exception);
            return;
        }

        log.error("Unknown error {} happened: {}", exception.getClass().getName(), exception.getMessage());
        exception.printStackTrace();
        AuthUtil.sendErrorResponse(response, new UnknownException());
    }
}
