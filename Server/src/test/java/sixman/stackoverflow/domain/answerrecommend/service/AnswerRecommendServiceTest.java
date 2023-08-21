package sixman.stackoverflow.domain.answerrecommend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.answerrecommend.answerrecommendrepository.AnswerRecommendRepository;
import sixman.stackoverflow.domain.answerrecommend.entity.AnswerRecommend;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.testhelper.ServiceTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AnswerRecommendServiceTest extends ServiceTest {
    @Autowired
    private AnswerRecommendRepository answerRecommendRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private  AnswerRepository answerRepository;
    @Autowired
    private AnswerRecommendService answerRecommendService;






    @Test
    @DisplayName("댓글의 추천 수가 자체가 없을때 , 추천하면 총 추천에 관한 수가 0에서 1로 증가한다.")
    void recommendAnswer1() {
        // Given
        Member member = createMember();
        memberRepository.save(member);

        setDefaultAuthentication(member.getMemberId());

        Question question = createquestion(member);
        questionRepository.save(question);

       Answer answer = createAnswerforRecommend(member, question);
        answerRepository.save(answer);


        // When
        answerRecommendService.recommendAnswer(answer.getAnswerId(), TypeEnum.UPVOTE);
        Answer updatedAnswer = answerRepository.findById(answer.getAnswerId()).orElse(null);


        // Then
        assertThat(updatedAnswer.getRecommend()).isEqualTo(1);
        assertThat(updatedAnswer.getAnswerId()).isEqualTo(answer.getAnswerId());
        assertThat(updatedAnswer.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(updatedAnswer.getQuestion().getQuestionId()).isEqualTo(question.getQuestionId());
        assertThat(updatedAnswer.getRecommend()).isEqualTo(answer.getRecommend());

    }

    @Test
    @DisplayName("댓글의 추천 수 자체가 없을때 , 비추천하면 총 추천에 관한 수가 0에서 -1로 감소한다.")
    void recommendAnswer2() {

        // Given
        Member member = createMember();
        memberRepository.save(member);

        setDefaultAuthentication(member.getMemberId());

        Question question = createquestion(member);
        questionRepository.save(question);

        Answer answer = createAnswerforRecommend(member, question);
        answerRepository.save(answer);


        // When
        answerRecommendService.recommendAnswer(answer.getAnswerId(), TypeEnum.DOWNVOTE);
        Answer updatedAnswer = answerRepository.findById(answer.getAnswerId()).orElse(null);


        // Then
        assertThat(updatedAnswer.getRecommend()).isEqualTo(-1);
        assertThat(updatedAnswer.getAnswerId()).isEqualTo(answer.getAnswerId());
        assertThat(updatedAnswer.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(updatedAnswer.getQuestion().getQuestionId()).isEqualTo(question.getQuestionId());
        assertThat(updatedAnswer.getRecommend()).isEqualTo(answer.getRecommend());
    }
    @Test
    @DisplayName("이미 추천을 한 상태에서 추천을 할 경우 추천을 취소하고 총 추천에 관한 수가 1에서 0으로 감소한다.")
    void recommendAnswer3() {

        // Given
        Member member = createMember();
        memberRepository.save(member);

        setDefaultAuthentication(member.getMemberId());

        Question question = createquestion(member);
        questionRepository.save(question);

        Answer answer = createAnswerforRecommend(member, question);
        answerRepository.save(answer);

        answerRecommendService.recommendAnswer(answer.getAnswerId(), TypeEnum.UPVOTE);


        // When & Then // 이미 추천 시 중복 추천
        answerRecommendService.recommendAnswer(answer.getAnswerId(), TypeEnum.UPVOTE);
        Answer updatedAnswer = answerRepository.findById(answer.getAnswerId()).orElse(null);
        assertThat(updatedAnswer.getRecommend()).isEqualTo(0);
        assertThat(updatedAnswer.getAnswerId()).isEqualTo(answer.getAnswerId());
        assertThat(updatedAnswer.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(updatedAnswer.getQuestion().getQuestionId()).isEqualTo(question.getQuestionId());
        assertThat(updatedAnswer.getRecommend()).isEqualTo(answer.getRecommend());
    }

    @Test
    @DisplayName("이미 비추천을 한 상태에서 비추천을 할 경우 비추천을 취소하고 총 추천에 관한 수가 -1에서 0으로 증가한다.")
    void recommendAnswer4() {

        // Given
        Member member = createMember();
        memberRepository.save(member);

        setDefaultAuthentication(member.getMemberId());

        Question question = createquestion(member);
        questionRepository.save(question);

        Answer answer = createAnswerforRecommend(member, question);
        answerRepository.save(answer);

        answerRecommendService.recommendAnswer(answer.getAnswerId(), TypeEnum.DOWNVOTE);


        // When & Then // 이미 비추천 시 중복 비추천
        answerRecommendService.recommendAnswer(answer.getAnswerId(), TypeEnum.DOWNVOTE);
        Answer updatedAnswer = answerRepository.findById(answer.getAnswerId()).orElse(null);
        assertThat(updatedAnswer.getRecommend()).isEqualTo(0);
        assertThat(updatedAnswer.getAnswerId()).isEqualTo(answer.getAnswerId());
        assertThat(updatedAnswer.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(updatedAnswer.getQuestion().getQuestionId()).isEqualTo(question.getQuestionId());
        assertThat(updatedAnswer.getRecommend()).isEqualTo(answer.getRecommend());
    }

    @Test
    @DisplayName("이미 추천을 한 상태에서 비추천을 할 경우 추천을 취소하고 비추천이 증가해 총 추천에 관한 수가 1에서 -1로 변경된다.")
    void recommendAnswer5() {

        // Given
        Member member = createMember();
        memberRepository.save(member);

        setDefaultAuthentication(member.getMemberId());

        Question question = createquestion(member);
        questionRepository.save(question);

        Answer answer = createAnswerforRecommend(member, question);
        answerRepository.save(answer);

        answerRecommendService.recommendAnswer(answer.getAnswerId(), TypeEnum.UPVOTE);


        // When
        answerRecommendService.recommendAnswer(answer.getAnswerId(), TypeEnum.DOWNVOTE);


        // Then
        Answer updatedAnswer = answerRepository.findById(answer.getAnswerId()).orElse(null);
        assertThat(updatedAnswer.getRecommend()).isEqualTo(-1);
        assertThat(updatedAnswer.getAnswerId()).isEqualTo(answer.getAnswerId());
        assertThat(updatedAnswer.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(updatedAnswer.getQuestion().getQuestionId()).isEqualTo(question.getQuestionId());
        assertThat(updatedAnswer.getRecommend()).isEqualTo(answer.getRecommend());

    }

    @Test
    @DisplayName("이미 비추천을 한 상태에서 추천을 할 경우 비추천을 취소하고 추천이 증가해 총 추천에 관한 수가 1로 증가한다.")
    void recommendAnswer6() {

        // Given
        Member member = createMember();
        memberRepository.save(member);

        setDefaultAuthentication(member.getMemberId());

        Question question = createquestion(member);
        questionRepository.save(question);

        Answer answer = createAnswerforRecommend(member, question);
        answerRepository.save(answer);

        answerRecommendService.recommendAnswer(answer.getAnswerId(), TypeEnum.DOWNVOTE);


        // When
        answerRecommendService.recommendAnswer(answer.getAnswerId(), TypeEnum.UPVOTE);


        // Then
        Answer updatedAnswer = answerRepository.findById(answer.getAnswerId()).orElse(null);
        assertThat(updatedAnswer.getRecommend()).isEqualTo(1);
        assertThat(updatedAnswer.getAnswerId()).isEqualTo(answer.getAnswerId());
        assertThat(updatedAnswer.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(updatedAnswer.getQuestion().getQuestionId()).isEqualTo(question.getQuestionId());
        assertThat(updatedAnswer.getRecommend()).isEqualTo(answer.getRecommend());
    }


    private Answer createAnswerforRecommend(Member member, Question question) {
        return Answer.builder()
                .member(member)
                .question(question)
                .content("test for recommend")
                .recommend(null)
                .build();
    }
}