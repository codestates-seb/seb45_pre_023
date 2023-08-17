package sixman.stackoverflow.global.exception.businessexception.requestexception;

import org.springframework.http.HttpStatus;

public class RequestNotAllowedException extends RequestException {

    public static final String MESSAGE = "Http Method 가 잘못되었습니다.";
    public static final String CODE = "REQUEST-405";

    public RequestNotAllowedException() {
        super(CODE, HttpStatus.METHOD_NOT_ALLOWED, MESSAGE);
    }

    public RequestNotAllowedException(String allowedMethod) {
        super(CODE, HttpStatus.METHOD_NOT_ALLOWED, MESSAGE + " 허용된 메소드 : " + allowedMethod);
    }
}
