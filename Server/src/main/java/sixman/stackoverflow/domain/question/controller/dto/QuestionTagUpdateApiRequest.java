package sixman.stackoverflow.domain.question.controller.dto;

import lombok.Getter;
import lombok.Setter;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class QuestionTagUpdateApiRequest {

    @NotBlank(message = "{validation.question.tag}")
    private String tagName;

}
