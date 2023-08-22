package sixman.stackoverflow.auth.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sixman.stackoverflow.auth.jwt.dto.LoginDto;
import sixman.stackoverflow.auth.jwt.service.TokenProvider;
import sixman.stackoverflow.auth.utils.AuthConstant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static sixman.stackoverflow.auth.utils.AuthConstant.*;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        checkRequestPOST(request);

        LoginDto loginDto = getLoginDtoFrom(request);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    private void checkRequestPOST(HttpServletRequest request) {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
    }

    private LoginDto getLoginDtoFrom(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(request.getInputStream(), LoginDto.class);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws ServletException, IOException {
        
        String accessToken = tokenProvider.generateAccessToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = tokenProvider.generateRefreshToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);

        response.setHeader(AUTHORIZATION, BEARER + accessToken);
        response.setHeader(REFRESH, BEARER + refreshToken);

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authentication);
    }
}
