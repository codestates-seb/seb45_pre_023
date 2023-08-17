package sixman.stackoverflow.global.exception.businessexception.answerexception;

import org.springframework.http.HttpStatus;

public class AnswerNotFoundException extends AnswerException{

    public static final String MESSAGE = "없거나 찾을 수 없는 답변입니다.";
    public static final String CODE = "ANSWER-404";



    public AnswerNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}

