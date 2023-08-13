package sixman.stackoverflow.global.exception.businessexception.s3exception;

import org.springframework.http.HttpStatus;

public class S3UploadException extends S3Exception{


    public static final String MESSAGE = "업로드에 실패했습니다. 다시 시도해주세요.";
    public static final String CODE = "S3-400";

    public S3UploadException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
