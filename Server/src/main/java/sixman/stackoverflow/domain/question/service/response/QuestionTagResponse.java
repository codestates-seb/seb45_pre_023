package sixman.stackoverflow.domain.question.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class QuestionTagResponse {

    private Long questionTagId;
    private Long questionId;
    private String tagName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static QuestionTagResponse from(QuestionTag questionTag) {
        return QuestionTagResponse.builder()
                .questionTagId(questionTag.getQuestionTagId())
                .questionId(questionTag.getQuestion().getQuestionId())
                .tagName(questionTag.getTag().getTagName())
                .createdDate(questionTag.getCreatedDate())
                .updatedDate(questionTag.getModifiedDate())
                .build();
    }
}