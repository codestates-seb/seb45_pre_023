package sixman.stackoverflow.domain.answerrecommend.entity;

import lombok.*;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.global.entity.BaseEntity;
import sixman.stackoverflow.global.entity.TypeEnum;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerRecommend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AnswerRecommendId;

    public void setType(TypeEnum type) {
        this.type = type;
    }

    @Enumerated(EnumType.STRING)
    private TypeEnum type;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

}
