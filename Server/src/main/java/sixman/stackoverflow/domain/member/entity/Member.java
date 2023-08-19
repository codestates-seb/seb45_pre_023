package sixman.stackoverflow.domain.member.entity;

import lombok.*;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answerrecommend.entity.AnswerRecommend;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.global.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "my_info_id")
    private MyInfo myInfo;

    private boolean enabled;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<QuestionRecommend> questionRecommends = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<AnswerRecommend> answerRecommends = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @Builder
    private Member(Long memberId, String email, String nickname, String password, Authority authority, MyInfo myInfo, boolean enabled){
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.authority = authority;
        this.myInfo = myInfo;
        this.enabled = enabled;
    }


    public static Member createMember(String email, String nickname, String password) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().build())
                .enabled(true)
                .build();
    }

    public void updateNickname(String nickname) {
        if(nickname != null) this.nickname = nickname;
    }

    public void updateMyInfo(String myIntro, String title, String location, List<String> accounts){
        this.myInfo.updateMyInfo(myIntro, title, location, accounts);
    }

    public void updatePassword(String newPassword) {

        this.password = newPassword;
    }

    public void updateImagePath(String imageType) {
        this.myInfo.updateImage(String.format("images/%s.%s", this.getEmail(), imageType));
    }

    public void disable() {
        this.enabled = false;
    }

    public void enable() {
        this.enabled = true;
    }


}
