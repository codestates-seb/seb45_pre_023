package sixman.stackoverflow.domain.questiontag.entity;

import lombok.*;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.global.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long QuestionTagId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public static QuestionTag createQuestionTag(Question question, Tag tag) {
        return QuestionTag.builder()
                .question(question)
                .tag(tag)
                .build();
    }
}

