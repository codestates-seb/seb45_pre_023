package sixman.stackoverflow.global.exception.businessexception.questionexception;

import org.springframework.http.HttpStatus;

public class QuestionNotFoundException extends QuestionException {

    public static final String MESSAGE = "질문을 찾을 수 없습니다.";
    public static final String CODE = "QUESTION-404";

    public QuestionNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
