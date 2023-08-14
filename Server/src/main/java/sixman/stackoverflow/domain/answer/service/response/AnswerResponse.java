package sixman.stackoverflow.domain.answer.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.entity.TypeEnum;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Builder
@AllArgsConstructor
public class AnswerResponse {

    private Long answerId;
    private String content;
    private Integer upvoteCount;
    private Integer downvoteCount;
    private List<ReplyResponse> replies;


    }

