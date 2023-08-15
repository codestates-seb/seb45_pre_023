package sixman.stackoverflow.domain.question.controller.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class QuestionTagCreateApiRequest {

    @NotBlank(message = "{validation.question.tag}")
    private String tagName;
}
