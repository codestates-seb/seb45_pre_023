package sixman.stackoverflow.domain.questionrecommend.entity;

import lombok.*;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.global.entity.BaseEntity;
import sixman.stackoverflow.global.entity.TypeEnum;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionRecommend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long QuestionRecommendId;

    @Enumerated(EnumType.STRING)
    private TypeEnum type;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public Long getRecommendId() {
        return QuestionRecommendId;
    }
    public void applyRecommend() {
        if (this.type == TypeEnum.UPVOTE) {
            this.question.applyRecommend(TypeEnum.UPVOTE);
        } else if (this.type == TypeEnum.DOWNVOTE) {
            this.question.applyRecommend(TypeEnum.DOWNVOTE);
        }
    }
}

