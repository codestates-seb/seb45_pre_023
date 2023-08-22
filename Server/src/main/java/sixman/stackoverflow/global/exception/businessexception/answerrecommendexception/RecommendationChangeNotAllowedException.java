package sixman.stackoverflow.global.exception.businessexception.answerrecommendexception;

import org.springframework.http.HttpStatus;

public class RecommendationChangeNotAllowedException extends AnswerRecommendException{
    public static final String MESSAGE = "먼저 투표를 취소하십시오";
    public static final String CODE = "ANSWERRECOMMEND-400";

    public RecommendationChangeNotAllowedException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);}
}
