package sixman.stackoverflow.global.exception.businessexception.answerrecommendexception;

import org.springframework.http.HttpStatus;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;

public abstract class AnswerRecommendException extends BusinessException {

    protected AnswerRecommendException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
