package sixman.stackoverflow.global.exception.businessexception.s3exception;

import org.springframework.http.HttpStatus;

public class S3FileNotValidException extends S3Exception{


    public static final String MESSAGE = "유효하지 않은 file 확장자입니다.";
    public static final String CODE = "S3-400";

    public S3FileNotValidException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
