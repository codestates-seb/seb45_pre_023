package sixman.stackoverflow.global.exception.businessexception.commonexception;

import org.springframework.http.HttpStatus;

public class UnknownException extends CommonException {

    public static final String MESSAGE = "알 수 없는 오류입니다. 다시 시도해주세요.";
    public static final String CODE = "COMMON-500";

    public UnknownException() {
        super(CODE, HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }

    public UnknownException(String message) {
        super(CODE, HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE + ":" + message);
    }
}
