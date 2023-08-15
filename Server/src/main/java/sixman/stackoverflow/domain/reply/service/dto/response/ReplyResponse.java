package sixman.stackoverflow.domain.reply.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.domain.reply.entity.Reply;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReplyResponse {

    private Long replyId;
    private String content;
    private Long memberId;


    public static ReplyResponse createReplyResponse(Reply reply, Long memberId) {


        return ReplyResponse.builder()
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .memberId(memberId)
                .build();

    }
}
