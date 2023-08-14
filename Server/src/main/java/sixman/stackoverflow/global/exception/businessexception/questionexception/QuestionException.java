package sixman.stackoverflow.global.exception.businessexception.questionexception;

import org.springframework.http.HttpStatus;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;

public abstract class QuestionException extends BusinessException {

    protected QuestionException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
