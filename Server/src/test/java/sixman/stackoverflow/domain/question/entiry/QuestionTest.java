package sixman.stackoverflow.domain.question.entiry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.testhelper.ServiceTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;



public class QuestionTest extends ServiceTest {

    @Test
    @DisplayName("questionTags를 받아 Question의 questionTags로 설정한다.")
    void setQuestionTags(){
        // given
        Member member = createMember();
        Question question = createQuestion(member);

        Tag tag1 = createTag("tag1");

        QuestionTag questionTag1 = QuestionTag.builder()
                .question(question)
                .tag(tag1)
                .build();

        // when
        question.setQuestionTags(List.of(questionTag1));


        // then
        assertThat(question.getQuestionTags()).containsExactly(questionTag1);
    }

    @Test
    @DisplayName("Question의 questionRecommends에서 추천과 비추천을 계산하여 추천수에 적용한다.")
    void applyRecommend(){
        Member member1 = createMember();
        Member member2 = createMember();
        Member member3 = createMember();

        Question question = createQuestionDetail(member1,0);

        QuestionRecommend upvote1 = QuestionRecommend.builder()
                .question(question)
                .member(member1)
                .type(TypeEnum.UPVOTE)
                .build();
        QuestionRecommend upvote2 = QuestionRecommend.builder()
                .question(question)
                .member(member2)
                .type(TypeEnum.DOWNVOTE)
                .build();
        QuestionRecommend downvote = QuestionRecommend.builder()
                .question(question)
                .member(member3)
                .type(TypeEnum.DOWNVOTE)
                .build();

        List<QuestionRecommend> questionRecommends = List.of(upvote1, upvote2, downvote);
        question.setQuestionRceommends(questionRecommends);

        // when
        question.applyRecommend();

        // then
        assertThat(question.getRecommend()).isEqualTo(-1);
    }

    @Test
    @DisplayName("membedId에 해당하는 사용자가 어떤 타입의 추천을 했는지 반환한다.")
    void getRecommendTypeCurrentUser(){
        // given
        Member member = createMemberDetail();
        Long currentUserId = 1L;

        QuestionRecommend recommend = QuestionRecommend.builder()
                .member(member)
                .type(TypeEnum.UPVOTE)
                .build();


        // when
        TypeEnum result = recommend.getRecommendTypeCurrentUser(currentUserId);

        // then
        assertThat(result).isEqualTo(TypeEnum.UPVOTE);
    }

    @Test
    @DisplayName("membedId에 해당하는 사용자와 로그인한 사용자가 다를경우 null을 반환한다.")
    void getRecommendTypeCurrentUserNullTest(){
        // given
        Member member = createMemberDetail();
        Long currentUserId = 2L;

        QuestionRecommend recommend = QuestionRecommend.builder()
                .member(member)
                .type(TypeEnum.UPVOTE)
                .build();


        // when
        TypeEnum result = recommend.getRecommendTypeCurrentUser(currentUserId);

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("question과 tag를 받아 QuestionTag로 반환한다.")
    void createQuestionTag(){
        // given
        Member member =createMember();
        Question question = createQuestion(member);
        Tag tag = createTag("test");

        // when
        QuestionTag questionTag = QuestionTag.createQuestionTag(question, tag);

        // then
        assertThat(questionTag).isNotNull();
        assertThat(questionTag.getQuestion()).isEqualTo(question);
        assertThat(questionTag.getTag()).isEqualTo(tag);
    }

    private Question createQuestion(Member member) {
        return Question.builder()
                .member(member)
                .detail("detail")
                .title("title")
                .expect("expect")
                .questionTags(new ArrayList<>())
                .build();
    }

    private Question createQuestionDetail(Member member, int num) {

        return Question.builder()
                .member(member)
                .detail("detail")
                .title("title")
                .expect("expect")
                .views(100+num)
                .recommend(num)
                .questionRecommends(new ArrayList<>())
                .questionTags(new ArrayList<>())
                .answers(new ArrayList<>())
                .build();
    }

    protected Tag createTag(String name){
        return Tag.builder()
                .tagName(name)
                .build();
    }

    protected Member createMemberDetail() {
        return Member.builder()
                .memberId(1L)
                .email("test@google.com")
                .nickname("test")
                .password(passwordEncoder.encode("1234abcd!"))
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().image("images/test.png").build())
                .enabled(true)
                .build();
    }
}
