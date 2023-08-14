package sixman.stackoverflow.domain.answer.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Builder
public class AnswerCreateApiRequest {

    @NotBlank(message = "{validation.answer.content}")
    private String content;
    private Integer views;
}
