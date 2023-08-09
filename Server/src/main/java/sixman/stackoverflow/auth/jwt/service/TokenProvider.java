package sixman.stackoverflow.auth.jwt.service;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import sixman.stackoverflow.global.exception.businessexception.authexception.JwtExpiredAuthException;
import sixman.stackoverflow.global.exception.businessexception.authexception.JwtNotVaildAuthException;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {


    private final Key key;
    private final CustomUserDetailsService userDetailsService;

    public TokenProvider (@Value("${jwt.secret-key}") String secretKey, CustomUserDetailsService userDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
    }

    public String generateAccessToken(Authentication authentication, long accessTokenExpireTime) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date date = new Date();
        long now = date.getTime();

        Date AccessTokenExpiresIn = new Date(now + accessTokenExpireTime);

        Long id = null;

        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            id = customUserDetails.getId();
        }

        if(authentication.getPrincipal() instanceof DefaultOAuth2User){
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            id =principal.getAttribute("id");
        }

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .claim("id", id)
                .setExpiration(AccessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication, long refreshTokenExpireTime){

        Date date = new Date();
        long now = date.getTime();

        Date refreshTokenExpiresIn = new Date(now + refreshTokenExpireTime);

        Long id = null;

        if(authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            id = customUserDetails.getId();
        }

        if(authentication.getPrincipal() instanceof DefaultOAuth2User){
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            id =principal.getAttribute("id");
        }

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("id", id)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateAccessTokenFromRefreshToken(String refreshToken, long accessTokenExpireTime) {
        Claims claims = getParseClaims(refreshToken);
        String username = claims.getSubject();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        return generateAccessToken(authentication, accessTokenExpireTime);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new JwtNotVaildAuthException();
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredAuthException();
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new JwtNotVaildAuthException();
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new JwtNotVaildAuthException();
        }
    }

    public Claims getParseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredAuthException();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new JwtNotVaildAuthException();
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new JwtNotVaildAuthException();
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new JwtNotVaildAuthException();
        }
    }


}
