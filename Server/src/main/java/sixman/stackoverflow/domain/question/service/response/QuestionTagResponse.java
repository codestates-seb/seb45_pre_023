package sixman.stackoverflow.domain.question.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.tag.entity.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class QuestionTagResponse {

    private Long questionTagId;
    private String tagName;

    public static List<QuestionTagResponse> of(List<Tag> tags) {
        List<QuestionTagResponse> questionTagResponses = new ArrayList<>();

        for (Tag tag : tags) {
            questionTagResponses.add(QuestionTagResponse.builder()
                    .questionTagId(tag.getTagId())
                    .tagName(tag.getTagName())
                    .build());
        }

        return questionTagResponses;
    }
}
