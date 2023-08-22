package sixman.stackoverflow.global.exception.businessexception.answerrecommendexception;

import org.springframework.http.HttpStatus;

public class DuplicateRecommendationException extends AnswerRecommendException{

    public static final String MESSAGE = "이미 추천한";
    public static final String CODE = "ANSWERRECOMMEND-409";

    public DuplicateRecommendationException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);}
}
