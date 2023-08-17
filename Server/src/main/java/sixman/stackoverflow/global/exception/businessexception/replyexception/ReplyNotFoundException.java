package sixman.stackoverflow.global.exception.businessexception.replyexception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReplyNotFoundException extends ReplyException {

    public static final String MESSAGE = "없거나 찾을 수 없는 댓글입니다.";
    public static final String CODE = "REPLY-404";



    public ReplyNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}

