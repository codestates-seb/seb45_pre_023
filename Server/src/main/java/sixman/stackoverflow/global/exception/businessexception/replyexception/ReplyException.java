package sixman.stackoverflow.global.exception.businessexception.replyexception;

import org.springframework.http.HttpStatus;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;

public abstract class ReplyException extends BusinessException {
    protected ReplyException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}

