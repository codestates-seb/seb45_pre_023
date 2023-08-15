package sixman.stackoverflow.domain.question.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.PageInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class QuestionResponse {

    private Long questionId;
    private String title;
    private String content;
    private int views;
    private List<QuestionRecommendDto> questionRecommends;
    private List<QuestionTags> tags;
    private QuestionAnswer answers;
    private Member member;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class QuestionTags{
        private Long tagId;
        private String tagName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class QuestionAnswer{
        private Long answerId;
        private Long memberId;
        private String content;
        private Integer recommend;
        private AnswerReplyPageResponse reply;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class AnswerReplyPageResponse{
        private List<Reply> reply;
        private PageInfo pageInfo;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Reply{
        private Long replyId;
        private Long memberId;
        private String content;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class QuestionRecommendDto{
        private Long recommendId;
        private Long memberId;
        private TypeEnum type;
    }


    public static QuestionResponse from(Question question) {
        List<QuestionRecommendDto> questionRecommends = question.getQuestionRecommends().stream()
                .map(recommend -> QuestionRecommendDto.builder()
                        .recommendId(recommend.getRecommendId())
                        .memberId(recommend.getMember().getMemberId())
                        .type(recommend.getType())
                        .build())
                .collect(Collectors.toList());

        List<QuestionTags> tags = question.getQuestionTags().stream()
                .map(tag -> QuestionTags.builder()
                        .tagId(tag.getTag().getTagId())
                        .tagName(tag.getTag().getTagName())
                        .build())
                .collect(Collectors.toList());

        List<QuestionAnswer> answers = question.getAnswers().stream()
                .map(answer -> QuestionAnswer.builder()
                        .answerId(answer.getAnswerId())
                        .memberId(answer.getMember().getMemberId())
                        .content(answer.getContent())
                        .recommend(answer.getRecommend())
                        // You might need to implement similar mapping for the reply and other fields
                        .build())
                .collect(Collectors.toList());

        return QuestionResponse.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .content(question.getContent())
                .views(question.getViews())
                .questionRecommends(questionRecommends)
                .tags(tags)
                .answers(answers)
                .member(question.getMember())
                .createdDate(question.getCreatedDate())
                .updatedDate(question.getUpdatedDate())
                .build();
    }

    public static QuestionResponse fromQuestion(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .content(question.getContent())
                .views(question.getViews())
                .questionRecommends(question.getQuestionRecommends())
                .member(question.getMember())
                .tags(question.getQuestionTags())
                .createdDate(question.getCreatedDate())
                .updatedDate(question.getModifiedDate())
                .build();
    }
}
