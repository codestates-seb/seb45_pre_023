package sixman.stackoverflow.global.exception.businessexception.memberexception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberDisabledException extends MemberException {

    public static final String MESSAGE = "탈퇴한 회원입니다.";
    public static final String CODE = "MEMBER-401";

    public MemberDisabledException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
