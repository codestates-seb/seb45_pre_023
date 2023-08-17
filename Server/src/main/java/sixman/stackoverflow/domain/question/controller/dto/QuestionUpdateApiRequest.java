package sixman.stackoverflow.domain.question.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sixman.stackoverflow.domain.question.entity.Question;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
@Builder
public class QuestionUpdateApiRequest {

    @NotBlank(message = "{validation.question.title}")
    private String title;

    @NotBlank(message = "{validation.question.content}")
    private String detail;

    @NotBlank(message = "{validation.question.content}")
    private String expect;

    // 업데이트 메서드
    public Question updateEntity(Question question) {
        question.setTitle(this.title);
        question.setDetail(this.detail);
        question.setExpect(this.expect);
        return question;
    }
}