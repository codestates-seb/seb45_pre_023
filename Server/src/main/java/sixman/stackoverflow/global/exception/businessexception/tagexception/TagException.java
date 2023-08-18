package sixman.stackoverflow.global.exception.businessexception.tagexception;

import org.springframework.http.HttpStatus;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;

public abstract class TagException extends BusinessException {

    protected TagException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
