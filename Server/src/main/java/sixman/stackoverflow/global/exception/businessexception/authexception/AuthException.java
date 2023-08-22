package sixman.stackoverflow.global.exception.businessexception.authexception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;

public abstract class AuthException extends BusinessException {

    protected AuthException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
