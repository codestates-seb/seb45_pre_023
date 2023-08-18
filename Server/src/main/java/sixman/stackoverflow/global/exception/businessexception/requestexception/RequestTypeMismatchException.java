package sixman.stackoverflow.global.exception.businessexception.requestexception;

import org.springframework.http.HttpStatus;

public class RequestTypeMismatchException extends RequestException {

    public static final String MESSAGE = "요청 값의 타입이 잘못되었습니다.";
    public static final String CODE = "REQUEST-405";

    public RequestTypeMismatchException() {
        super(CODE, HttpStatus.METHOD_NOT_ALLOWED, MESSAGE);
    }

    public RequestTypeMismatchException(String failedValue) {
        super(CODE, HttpStatus.METHOD_NOT_ALLOWED, MESSAGE + " 잘못된 값 : " + failedValue);
    }
}
