package sixman.stackoverflow.domain.reply.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sixman.stackoverflow.domain.answer.service.request.AnswerCreateServiceRequest;
import sixman.stackoverflow.domain.reply.service.dto.request.ReplyCreateServiceRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCreateApiRequest {

    @NotBlank(message = "{validation.reply.content}")
    private String content;

    public ReplyCreateServiceRequest toServiceRequest() {
        return ReplyCreateServiceRequest.builder()
                .content(content)
                .build();
    }



}
