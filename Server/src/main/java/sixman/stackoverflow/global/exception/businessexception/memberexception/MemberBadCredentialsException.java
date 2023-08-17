package sixman.stackoverflow.global.exception.businessexception.memberexception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberBadCredentialsException extends MemberException {

    public static final String MESSAGE = "로그인 정보를 확인해주세요.";
    public static final String CODE = "MEMBER-401";

    public MemberBadCredentialsException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
