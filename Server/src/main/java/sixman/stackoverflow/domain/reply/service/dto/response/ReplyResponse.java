package sixman.stackoverflow.domain.reply.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReplyResponse {

    private Long replyId;
    private String content;

}
