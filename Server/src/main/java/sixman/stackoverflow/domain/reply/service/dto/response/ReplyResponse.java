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

    public static ReplyResponse createReplyResponse(Reply reply) {

        return ReplyResponse.builder()
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .member(MemberInfo.of(reply.getMember()))
                .updatedDate(reply.getCreatedDate())
                .createdDate(reply.getModifiedDate())
                .build();

    }

    public static ReplyResponse pagingReplyResponse(Reply reply) {
        return builder()
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .member(MemberInfo.of(reply.getMember()))
                .createdDate(reply.getCreatedDate())
                .updatedDate(reply.getModifiedDate())
                .build();
    }
}
