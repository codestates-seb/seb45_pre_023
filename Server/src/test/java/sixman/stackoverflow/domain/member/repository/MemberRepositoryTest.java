package sixman.stackoverflow.domain.member.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answerrecommend.entity.AnswerRecommend;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.dto.MemberAnswerData;
import sixman.stackoverflow.domain.member.repository.dto.MemberQuestionData;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.testhelper.RepositoryTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends RepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("email 을 받아 Member 객체를 반환한다.")
    void findByEmail() {

        //given
        Member member = createMember();
        em.persist(member);

        //when
        Member findMember = memberRepository.findByEmail(member.getEmail()).orElseThrow();

        //then
        assertThat(findMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("pageable 을 받아 해당 멤버의 MemberQuestionData 을 반환한다.")
    void findQuestionByMemberId() {
        //given
        Member member = createMember();
        em.persist(member);

        List<Question> questions = createQuestions(member, 10);
        questions.forEach(question -> {
            em.persist(question);
            em.persist(createQuestionRecommend(member, question));
        });

        PageRequest pageRequest = PageRequest.of(0, 5);

        //when
        Page<MemberQuestionData> findQuestions = memberRepository.findQuestionByMemberId(member.getMemberId(), pageRequest);

        //then
        assertThat(findQuestions.getTotalElements()).isEqualTo(10);
        assertThat(findQuestions.getTotalPages()).isEqualTo(2);
        assertThat(findQuestions.getNumberOfElements()).isEqualTo(5);
        assertThat(findQuestions.getContent().get(0).getRecommend()).isEqualTo(1);
    }
    
    @Test
    @DisplayName("pageable 을 받아 해당 멤버의 MemberAnswerData 를 반환한다.")
    void findAnswerByMemberId() {
        //given
        Member member = createMember();
        em.persist(member);

        List<Question> questions = createQuestions(member, 10);
        questions.forEach(question -> {
            em.persist(question);
            Answer answer = createAnswer(member, question);
            em.persist(answer);
            em.persist(createAnswerRecommend(member, answer));
        });

        PageRequest pageRequest = PageRequest.of(0, 5);

        //when
        Page<MemberAnswerData> findAnswers = memberRepository.findAnswerByMemberId(member.getMemberId(), pageRequest);

        //then
        assertThat(findAnswers.getTotalElements()).isEqualTo(10);
        assertThat(findAnswers.getTotalPages()).isEqualTo(2);
        assertThat(findAnswers.getNumberOfElements()).isEqualTo(5);
        assertThat(findAnswers.getContent().get(0).getRecommend()).isEqualTo(1);
    }

    @Test
    @DisplayName("memberId 를 받아 해당 멤버가 작성한 게시물의 모든 태그를 반환한다.")
    void findTagByMemberId() {
        //given
        Member member = createMember();
        em.persist(member);

        Question question1 = createQuestion(member);
        Question question2 = createQuestion(member);
        em.persist(question1);
        em.persist(question2);

        Tag tag1 = createTag("tag1");
        Tag tag2 = createTag("tag2");
        Tag tag3 = createTag("tag3");
        em.persist(tag1);
        em.persist(tag2);
        em.persist(tag3);

        QuestionTag questionTag1 = createQuestionTag(question1, tag1);
        QuestionTag questionTag2 = createQuestionTag(question2, tag2);
        em.persist(questionTag1);
        em.persist(questionTag2);

        //when
        List<Tag> tags = memberRepository.findTagByMemberId(member.getMemberId());

        //then
        assertThat(tags).hasSize(2)
                .extracting("tagName").containsExactly("tag1", "tag2");
    }

    private Question createQuestion(Member member) {
        return Question.builder()
                .title("title")
                .detail("detail")
                .expect("expect")
                .member(member)
                .build();
    }

    private List<Question> createQuestions(Member member, int count){
        List<Question> questions = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            questions.add(createQuestion(member));
        }
        return questions;
    }

    private QuestionRecommend createQuestionRecommend(Member member, Question question) {
        return QuestionRecommend.builder()
                .member(member)
                .question(question)
                .type(TypeEnum.UPVOTE)
                .build();
    }

    private Answer createAnswer(Member member, Question question) {
        return Answer.builder()
                .content("content")
                .member(member)
                .question(question)
                .build();
    }
    
    private AnswerRecommend createAnswerRecommend(Member member, Answer answer){
        return AnswerRecommend.builder()
                .member(member)
                .answer(answer)
                .type(TypeEnum.UPVOTE)
                .build();
    }

    private Tag createTag(String name){
        return Tag.builder()
                .tagName(name)
                .build();
    }

    private QuestionTag createQuestionTag(Question question, Tag tag){
        return QuestionTag.builder()
                .question(question)
                .tag(tag)
                .build();
    }
}