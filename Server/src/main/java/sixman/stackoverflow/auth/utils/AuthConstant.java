package sixman.stackoverflow.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConstant {

    public static long ACCESS_TOKEN_EXPIRE_TIME;
    public static long REFRESH_TOKEN_EXPIRE_TIME;

    @Value("${jwt.access-token-expire-time}")
    public void setAccessTokenExpireTime(long value) {
        ACCESS_TOKEN_EXPIRE_TIME = value;
    }

    @Value("${jwt.refresh-token-expire-time}")
    public void setRefreshTokenExpireTime(long value) {
        REFRESH_TOKEN_EXPIRE_TIME = value;
    }
}
