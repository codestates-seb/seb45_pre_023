package sixman.stackoverflow.domain.reply.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class ReplyUpdateApiRequest {


    @NotBlank(message = "{validation.reply.content}")
    private String content;
}
