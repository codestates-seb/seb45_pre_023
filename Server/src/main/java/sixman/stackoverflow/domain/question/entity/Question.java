package sixman.stackoverflow.domain.question.entity;

import lombok.*;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.global.entity.BaseEntity;
import sixman.stackoverflow.global.entity.TypeEnum;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String detail;

    @Column(nullable = false)
    private String expect;

    private Integer views;

    private int recommendCount;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionRecommend> questionRecommends = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionTag> questionTags = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public void setQuestionTags(List<QuestionTag> questionTags) {
        this.questionTags.clear();
        if (questionTags != null) {
            this.questionTags.addAll(questionTags);
        }
    }

    public void applyRecommend(TypeEnum type) {
        if (type == TypeEnum.UPVOTE) {
            this.recommendCount++;
        } else {
            this.recommendCount--;
        }
    }

    public boolean hasRecommendationFrom(Member member) {
        return this.questionRecommends.stream()
                .anyMatch(recommend -> recommend.getMember().equals(member));
    }
}
