package sixman.stackoverflow.global.exception.businessexception.authexception;

import org.springframework.http.HttpStatus;

public class JwtNotVaildAuthException extends AuthException{

    public static final String MESSAGE = "유효하지 않은 JWT 토큰입니다.";
    public static final String CODE = "JWT-400";

    public JwtNotVaildAuthException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
