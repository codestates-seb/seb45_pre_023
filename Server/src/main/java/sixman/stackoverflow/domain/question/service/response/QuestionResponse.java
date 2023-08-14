package sixman.stackoverflow.domain.question.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class QuestionResponse {

    private Long questionId;
    private String title;
    private String content;
    private int views;
    private List<QuestionRecommend> questionRecommends;
    private List<QuestionTag> tags;
    private List<Answer> answers;
    private Member member;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static QuestionResponse from(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .content(question.getContent())
                .views(question.getViews())
                .questionRecommends(question.getQuestionRecommends())
                .answers(question.getAnswers())
                .member(question.getMember())
                .tags(question.getQuestionTags())
                .createdDate(question.getCreatedDate())
                .updatedDate(question.getModifiedDate())
                .build();
    }
}