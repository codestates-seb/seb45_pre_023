package sixman.stackoverflow.domain.question.controller.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class QuestionTagCreateApiRequest {

    @NotBlank(message = "태그를 선택해주세요.")
    private String tagName;
}
