package sixman.stackoverflow.domain.reply.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@AllArgsConstructor
public class ReplyCreateApiRequest {

    @NotBlank(message = "{validation.reply.content}")
    private String content;


}
