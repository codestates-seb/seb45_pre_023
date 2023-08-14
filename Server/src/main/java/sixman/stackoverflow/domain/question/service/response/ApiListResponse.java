package sixman.stackoverflow.domain.question.service.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ApiListResponse<T> {
    private final List<T> data;

    public ApiListResponse(List<T> data) {
        this.data = data;
    }

    public static <T> ApiListResponse<T> of(List<T> data) {
        return new ApiListResponse<>(data);
    }
}