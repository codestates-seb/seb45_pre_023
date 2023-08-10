package todolist.global.exception.exceptionhandler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import todolist.global.exception.buinessexception.BusinessException;
import todolist.global.exception.buinessexception.memberexception.MemberAccessDeniedException;
import todolist.global.reponse.ApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<ApiResponse.ErrorResponse>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        return ResponseEntity.badRequest().body(ApiResponse.fail(e));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<List<ApiResponse.ErrorResponse>>> handleConstraintViolationException(
            ConstraintViolationException e) {

        return ResponseEntity.badRequest().body(ApiResponse.fail(e));
    }

    //메서드 인가 검증 실패 시 호출하는 메서드 (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<BindingResult>> handleAccessDeniedException(AccessDeniedException e) {

        ApiResponse.fail(new MemberAccessDeniedException());

        return new ResponseEntity<>(ApiResponse.fail(new MemberAccessDeniedException()), HttpStatusCode.valueOf(403));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<BindingResult>> handleBusinessException(BusinessException e) {

        return new ResponseEntity<>(ApiResponse.fail(e), e.getHttpStatus());
    }

    //잘못된 메서드 요청
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        return new ResponseEntity<>(ApiResponse.fail(e), HttpStatusCode.valueOf(405));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<BindingResult>> handleException(Exception e) {


        e.printStackTrace();

        return new ResponseEntity<>(ApiResponse.fail(e), HttpStatusCode.valueOf(500));
    }
}
