package sixman.stackoverflow.domain.reply.entity;

import lombok.*;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.global.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    public static Reply createReply(String content, Answer answer, Member member) {
        return Reply.builder()
                .content(content)
                .answer(answer)
                .member(member)
                .build();
    }
    public void setReplyContent(String newContent) {
        this.content = newContent;

    }
}

