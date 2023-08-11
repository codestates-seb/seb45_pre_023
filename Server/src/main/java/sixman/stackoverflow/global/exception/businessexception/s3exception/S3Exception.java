package sixman.stackoverflow.global.exception.businessexception.s3exception;

import org.springframework.http.HttpStatus;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;

public abstract class S3Exception extends BusinessException {
    protected S3Exception(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
