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

import static sixman.stackoverflow.auth.utils.AuthConstant.ACCESS_TOKEN_EXPIRE_TIME;

@RequiredArgsConstructor
public class JwtRefreshFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final MemberRefreshFailureHandler refreshFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //POST 방식이 아니면 refresh 페이지를 포함해서 다시 예외를 던진다.
        if(!request.getMethod().equals("POST")){
            this.refreshFailureHandler.onAuthenticationFailure(request, response, new RequestNotAllowedException());
        }
        else{
            try {
                String refreshToken = getRefreshToken(request);
                if(refreshToken == null){
                    throw new JwtNotFoundAuthException();
                }
                tokenProvider.validateToken(refreshToken);

                String regeneratedAccessToken = tokenProvider.generateAccessTokenFromRefreshToken(refreshToken, ACCESS_TOKEN_EXPIRE_TIME);

                response.setHeader("authorization", "Bearer " + regeneratedAccessToken);

            //모든 예외는 이곳에서 처리된다.
            }catch(Exception exception){
                this.refreshFailureHandler.onAuthenticationFailure(request, response, exception);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        return !request.getRequestURI().equals("/auth/refresh");
    }

    private String getRefreshToken(HttpServletRequest request) {

        return request.getHeader("Refresh").replace("Bearer ", "");
    }
}
