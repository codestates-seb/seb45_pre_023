package sixman.stackoverflow.domain.question.entity;

import lombok.*;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.global.entity.BaseEntity;
import sixman.stackoverflow.global.entity.TypeEnum;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Lob
    @Column(nullable = false)
    private String detail;

    @Lob
    @Column(nullable = false)
    private String expect;

    private int views;

    private int recommend;

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

    public void setViews(int views) {
        this.views = views;
    }

    public void setRecommend(int recommend) { this.recommend = recommend; }

    public void setQuestionRceommends(List<QuestionRecommend> questionRecommends) { this.questionRecommends = questionRecommends; }


    public void setQuestionTags(List<QuestionTag> questionTags) {
        this.questionTags.clear();
        if (questionTags != null) {
            this.questionTags.addAll(questionTags);
        }
    }

    public void applyRecommend() {
        int upvotes = (int) this.questionRecommends.stream()
                .filter(recommend -> recommend.getType() == TypeEnum.UPVOTE)
                .count();
        int downvotes = (int) this.questionRecommends.stream()
                .filter(recommend -> recommend.getType() == TypeEnum.DOWNVOTE)
                .count();

        this.recommend = upvotes - downvotes;
    }
}
