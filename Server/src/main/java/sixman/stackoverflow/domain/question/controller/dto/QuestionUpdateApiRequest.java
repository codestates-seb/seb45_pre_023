package sixman.stackoverflow.domain.question.controller.dto;

import lombok.Getter;
import lombok.Setter;
import sixman.stackoverflow.domain.question.entity.Question;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class QuestionUpdateApiRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    // 업데이트 메서드
    public Question updateEntity(Question question) {
        question.setTitle(this.title);
        question.setContent(this.content);
        return question;
    }
}