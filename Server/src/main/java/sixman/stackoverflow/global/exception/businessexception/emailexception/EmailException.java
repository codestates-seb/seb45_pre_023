package sixman.stackoverflow.global.exception.businessexception.emailexception;

import org.springframework.http.HttpStatus;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;

public abstract class EmailException extends BusinessException {

    protected EmailException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
