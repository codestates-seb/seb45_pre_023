package sixman.stackoverflow.domain.reply.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ReplyCreateServiceRequest {

    private String content;
}
