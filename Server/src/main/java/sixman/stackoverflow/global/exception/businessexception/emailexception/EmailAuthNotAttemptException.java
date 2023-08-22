package sixman.stackoverflow.global.exception.businessexception.emailexception;

import org.springframework.http.HttpStatus;
import sixman.stackoverflow.global.exception.businessexception.commonexception.CommonException;

public class EmailAuthNotAttemptException extends CommonException {

    public static final String MESSAGE = "이메일 인증을 먼저 시도해주세요.";
    public static final String CODE = "EMAIL-400";

    public EmailAuthNotAttemptException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
