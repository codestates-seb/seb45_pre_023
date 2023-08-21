package sixman.stackoverflow.domain.answer.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answerrecommend.entity.AnswerRecommend;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.service.response.QuestionDetailResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionTagResponse;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.PageInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public static AnswerResponse createAnswerResponse(Answer answer, TypeEnum answerRecommendType) {

        return AnswerResponse.builder()

                .answerId(answer.getAnswerId())
                .content(answer.getContent())
                .member(MemberInfo.of(answer.getMember()))
                .recommend(answer.getRecommend())
                .recommendType(answerRecommendType)
                .updatedDate(answer.getCreatedDate())
                .createdDate(answer.getModifiedDate())
                .build();

    }

    public static AnswerResponse of(Answer answer, Page<Reply> replyPage) {
        TypeEnum recommendType = null;
        if(SecurityUtil.isLogin()) {
            Long currentUserId = SecurityUtil.getCurrentId();
            for (AnswerRecommend recommend : answer.getAnswerRecommends()) {
                TypeEnum type = recommend.getRecommendTypeCurrentUser(currentUserId);
                if (type != null) {
                    recommendType = type;
                    break;
                }
            }
        }

        List<ReplyResponse> replyResponses = replyPage.getContent().stream()
                .map(ReplyResponse::pagingReplyResponse)
                .collect(Collectors.toList());

        PageInfo pageInfo = PageInfo.of(replyPage);

        AnswerReply answerReply = AnswerReply.builder()
                .replies(replyResponses)
                .pageInfo(pageInfo)
                .build();

        return AnswerResponse.builder()
                .answerId(answer.getAnswerId())
                .content(answer.getContent())
                .member(MemberInfo.of(answer.getMember()))
                .recommend(answer.getRecommend())
                .recommendType(recommendType)
                .reply(answerReply)
                .updatedDate(answer.getCreatedDate())
                .createdDate(answer.getModifiedDate())
                .build();
    }
}

