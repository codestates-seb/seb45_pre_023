package sixman.stackoverflow.global.exception.businessexception.authexception;

import org.springframework.http.HttpStatus;

public class JwtNotFoundAuthException extends AuthException{

    public static final String MESSAGE = "토큰 정보가 필요합니다.";
    public static final String CODE = "JWT-400";

    public JwtNotFoundAuthException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
