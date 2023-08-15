package sixman.stackoverflow.domain.answer.entitiy;

import lombok.*;
import sixman.stackoverflow.domain.answerrecommend.entity.AnswerRecommend;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.global.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @Column(nullable = false)
    private String content;

    private Integer upvoteCount;

    private Integer downvoteCount;



    @OneToMany(mappedBy = "answer")
    private List<AnswerRecommend> answerRecommends = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToMany(mappedBy = "answer")
    private List<Reply> replies = new ArrayList<>();




    public static Answer createAnswers(String content, Member member, Question question) {
        return Answer.builder()
                .content(content)
                .member(member)
                .question(question)
                .build();
    }
    public void setAnswerContent(String newContent) {
        this.content = newContent;

    }

    public void increaseRecommendCount() {
        upvoteCount++;
    }

    public void increaseDownvoteCount() {
        downvoteCount++;
    }
}
