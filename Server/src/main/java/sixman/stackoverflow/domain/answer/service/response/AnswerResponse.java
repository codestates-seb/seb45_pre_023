package sixman.stackoverflow.domain.answer.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.PageInfo;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Builder
@AllArgsConstructor
public class AnswerResponse {

    private Long answerId;
    private MemberInfo member;
    private String content;
    private Integer recommend;
    private TypeEnum recommendType;
    private AnswerReply reply;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class AnswerReply{

        private List<ReplyResponse> replies;
        private PageInfo pageInfo;
    }
    public static AnswerResponse createAnswerResponse(Answer answer) {

        return AnswerResponse.builder()

                .answerId(answer.getAnswerId())
                .content(answer.getContent())
                .member(MemberInfo.of(answer.getMember()))
                .updatedDate(answer.getCreatedDate())
                .createdDate(answer.getModifiedDate())
                .build();

    }

}

