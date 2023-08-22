package sixman.stackoverflow.auth.jwt.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import sixman.stackoverflow.auth.jwt.handler.MemberRefreshFailureHandler;
import sixman.stackoverflow.auth.jwt.service.TokenProvider;
import sixman.stackoverflow.global.exception.businessexception.authexception.JwtNotFoundAuthException;
import sixman.stackoverflow.global.exception.businessexception.requestexception.RequestNotAllowedException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static sixman.stackoverflow.auth.utils.AuthConstant.*;

@RequiredArgsConstructor
public class JwtRefreshFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final MemberRefreshFailureHandler refreshFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //POST 방식이 아니면 refresh 페이지를 포함해서 다시 예외를 던진다.
        if(!isPOST(request)){
            this.refreshFailureHandler.onAuthenticationFailure(request, response, new RequestNotAllowedException("POST"));
        }
        else{
            try {
                String refreshToken = getRefreshToken(request);

                tokenProvider.validateToken(refreshToken);

                String regeneratedAccessToken =
                        tokenProvider.generateAccessTokenFrom(refreshToken, ACCESS_TOKEN_EXPIRE_TIME);

                response.setHeader(AUTHORIZATION, BEARER + regeneratedAccessToken);

            //모든 예외는 이곳에서 처리된다.
            }catch(Exception exception){
                this.refreshFailureHandler.onAuthenticationFailure(request, response, exception);
            }
        }
    }

    private boolean isPOST(HttpServletRequest request) {
        return request.getMethod().equals("POST");
    }

    private String getRefreshToken(HttpServletRequest request) {

        String refreshToken = request.getHeader(REFRESH);

        if(refreshToken == null){
            throw new JwtNotFoundAuthException();
        }

        return refreshToken.replace(BEARER, "");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        return !request.getRequestURI().equals(AUTH_REFRESH_URL);
    }
}
