package sixman.stackoverflow.domain.question.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.PageInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class QuestionDetailResponse {

    private Long questionId;
    private String title;
    private String detail;
    private String expect;
    private MemberInfo member;
    private Integer views;
    private Integer recommend;
    private TypeEnum recommendType;
    private List<QuestionTagResponse> tags;
    private QuestionAnswer answer;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class QuestionAnswer{
           private List<AnswerResponse> answers;
            private PageInfo pageInfo;
    }

    public static QuestionDetailResponse of(Question question, QuestionAnswer answer) {
        TypeEnum recommendType = null;
        if(SecurityUtil.isLogin()) {
            Long currentUserId = SecurityUtil.getCurrentId();
            for (QuestionRecommend recommend : question.getQuestionRecommends()) {
                TypeEnum type = recommend.getRecommendTypeCurrentUser(currentUserId);
                if (type != null) {
                    recommendType = type;
                    break;
                }
            }
        }

        return QuestionDetailResponse.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .detail(question.getDetail())
                .expect(question.getExpect())
                .member(MemberInfo.of(question.getMember()))
                .views(question.getViews())
                .recommend(question.getRecommend())
                .recommendType(recommendType)
                .tags(QuestionTagResponse.of(question.getQuestionTags().stream().map(QuestionTag::getTag).collect(Collectors.toList())))
                .answer(answer)
                .createdDate(question.getCreatedDate())
                .updatedDate(question.getModifiedDate())
                .build();
    }

}
