package sixman.stackoverflow.global.exception.businessexception.emailexception;

import org.springframework.http.HttpStatus;
import sixman.stackoverflow.global.exception.businessexception.commonexception.CommonException;

public class EmailAuthNotCompleteException extends CommonException {

    public static final String MESSAGE = "이메일 인증이 완료되지 않았습니다.";
    public static final String CODE = "EMAIL-400";

    public EmailAuthNotCompleteException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
