package sixman.stackoverflow.global.exception.exceptionhandler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.requestexception.RequestTypeMismatchException;
import sixman.stackoverflow.global.response.ApiSingleResponse;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiSingleResponse<List<ApiSingleResponse.ErrorResponse>>> handleBindException(
            BindException e) {

        return ResponseEntity.badRequest().body(ApiSingleResponse.fail(e));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiSingleResponse<List<ApiSingleResponse.ErrorResponse>>> handleConstraintViolationException(
            ConstraintViolationException e) {

        return ResponseEntity.badRequest().body(ApiSingleResponse.fail(e));
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ApiSingleResponse<Void>> handleTypeMismatchException(
            TypeMismatchException e) {

        String value = (String) e.getValue();

        return new ResponseEntity<>(ApiSingleResponse.fail(new RequestTypeMismatchException(value)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiSingleResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {

        ApiSingleResponse.fail(new MemberAccessDeniedException());

        return new ResponseEntity<>(ApiSingleResponse.fail(new MemberAccessDeniedException()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiSingleResponse<Void>> handleBusinessException(BusinessException e) {

        return new ResponseEntity<>(ApiSingleResponse.fail(e), e.getHttpStatus());
    }

    //잘못된 메서드 요청
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiSingleResponse<String>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        return new ResponseEntity<>(ApiSingleResponse.fail(e), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiSingleResponse<Void>> handleException(Exception e) {

        log.error("Unknown error happened: {}", e.getMessage());
        e.printStackTrace();

        return new ResponseEntity<>(ApiSingleResponse.fail(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
