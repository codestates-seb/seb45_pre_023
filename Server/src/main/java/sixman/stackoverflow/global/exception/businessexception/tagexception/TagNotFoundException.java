package sixman.stackoverflow.global.exception.businessexception.tagexception;

import org.springframework.http.HttpStatus;

public class TagNotFoundException extends TagException {

    public static final String MESSAGE = "태그를 찾을 수 없습니다.";
    public static final String CODE = "TAG-404";

    public TagNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
