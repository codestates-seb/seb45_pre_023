package sixman.stackoverflow.domain.answer.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sixman.stackoverflow.domain.answer.service.request.AnswerCreateServiceRequest;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class AnswerCreateApiRequest {

    @NotBlank(message = "{validation.answer.content}")
    private String content;


    public AnswerCreateServiceRequest toServiceRequest() {
        return AnswerCreateServiceRequest.builder()
                .content(content)
                .build();
    }
}
