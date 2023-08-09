package sixman.stackoverflow.global.response;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class ApiPageResponse<T> {

    private List<T> data;
    private PageInfo pageInfo;
    private int code;
    private String status;
    private String message;

    public static <T, P extends Page<T>> ApiPageResponse<T> ok(P data) {
        return ApiPageResponse.of(data, HttpStatus.OK);
    }

    public static <T, P extends Page<T>> ApiPageResponse<T> ok(P data, String message) {
        return ApiPageResponse.of(data, HttpStatus.OK, message);
    }

    public static <T, P extends Page<T>> ApiPageResponse<T> of(P data, HttpStatus httpStatus) {
        return ApiPageResponse.of(data, httpStatus, httpStatus.getReasonPhrase());
    }

    public static <T, P extends Page<T>> ApiPageResponse<T> of(P data, HttpStatus httpStatus, String message) {
        return new ApiPageResponse<>(
                data.getContent(),
                PageInfo.of(data),
                httpStatus.value(),
                httpStatus.name(),
                message);
    }
}
