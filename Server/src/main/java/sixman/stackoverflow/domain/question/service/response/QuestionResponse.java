package sixman.stackoverflow.domain.question.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.global.entity.TypeEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class QuestionResponse {

    private Long questionId;
    private String title;
    private String detail;
    private Integer answerCount;
    private MemberInfo member;
    private Integer views;
    private Integer recommend;
    private List<QuestionTagResponse> tags;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static QuestionResponse of(Question question) {

        return QuestionResponse.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .detail(question.getDetail())
                .answerCount(question.getAnswers().size())
                .member(MemberInfo.of(question.getMember()))
                .views(question.getViews())
                .recommend(question.getRecommendCount())
                .tags(QuestionTagResponse.of(question.getQuestionTags().stream().map(QuestionTag::getTag).collect(Collectors.toList())))
                .createdDate(question.getCreatedDate())
                .updatedDate(question.getModifiedDate())
                .build();
    }
}
