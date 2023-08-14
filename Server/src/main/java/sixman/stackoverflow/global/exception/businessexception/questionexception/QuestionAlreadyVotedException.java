package sixman.stackoverflow.global.exception.businessexception.questionexception;

import org.springframework.http.HttpStatus;

public class QuestionAlreadyVotedException extends QuestionException {
    public static final String MESSAGE = "이미 추천(비추천)을 하였습니다.";
    public static final String CODE = "QUESTION-400";

    public QuestionAlreadyVotedException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}

