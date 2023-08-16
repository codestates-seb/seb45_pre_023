package sixman.stackoverflow.domain.answer.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class AnswerUpdateApiRequest {
    @NotBlank(message = "{validation.answer.content}")
    private String content;

}