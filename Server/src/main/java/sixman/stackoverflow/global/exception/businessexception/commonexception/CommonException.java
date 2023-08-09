package sixman.stackoverflow.global.exception.businessexception.commonexception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;

public abstract class CommonException extends BusinessException {

    protected CommonException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }

}
