package sixman.stackoverflow.domain.reply.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.parameters.P;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.domain.reply.entity.Reply;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReplyResponse {

    private Long replyId;
    private String content;
    private MemberInfo member;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static ReplyResponse createReplyResponse(Reply reply, Member member) {

        return ReplyResponse.builder()
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .member(MemberInfo.of(member))
                .build();

    }
}
