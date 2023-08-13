package sixman.stackoverflow.global.exception.businessexception.answerexception;

import org.springframework.http.HttpStatus;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;

public abstract class AnswerException extends BusinessException {

    protected AnswerException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
