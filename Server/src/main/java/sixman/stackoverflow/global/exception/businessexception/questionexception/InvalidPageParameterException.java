package sixman.stackoverflow.global.exception.businessexception.questionexception;

import org.springframework.http.HttpStatus;

public class InvalidPageParameterException extends QuestionException {

    public static final String MESSAGE = "잘못된 페이지 파라미터입니다.";
    public static final String CODE = "QUESTION-400-INVALID-PARAMETER";

    public InvalidPageParameterException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
