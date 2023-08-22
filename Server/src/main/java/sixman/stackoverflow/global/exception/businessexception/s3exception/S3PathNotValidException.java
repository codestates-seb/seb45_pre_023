package sixman.stackoverflow.global.exception.businessexception.s3exception;

import org.springframework.http.HttpStatus;

public class S3PathNotValidException extends S3Exception{


    public static final String MESSAGE = "이미지 경로에 오류가 있습니다. 다시 시도해주세요.";
    public static final String CODE = "S3-400";

    public S3PathNotValidException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
