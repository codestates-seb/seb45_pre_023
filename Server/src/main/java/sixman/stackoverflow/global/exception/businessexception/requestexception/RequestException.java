package sixman.stackoverflow.global.exception.businessexception.requestexception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;

public abstract class RequestException extends BusinessException {

    protected RequestException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }

}
