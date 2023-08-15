package sixman.stackoverflow.domain.question.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sixman.stackoverflow.domain.question.entity.Question;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class QuestionCreateApiRequest {

    private Long memberId;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    // 생성 메서드
    public Question toEntity() {
        return Question.builder()
                .title(title)
                .content(content)
                .build();
    }
}