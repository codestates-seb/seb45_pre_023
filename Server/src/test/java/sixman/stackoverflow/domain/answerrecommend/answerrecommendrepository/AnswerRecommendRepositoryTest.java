package sixman.stackoverflow.domain.answerrecommend.answerrecommendrepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.answer.service.AnswerService;
import sixman.stackoverflow.domain.answerrecommend.entity.AnswerRecommend;
import sixman.stackoverflow.domain.answerrecommend.service.AnswerRecommendService;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.testhelper.RepositoryTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AnswerRecommendRepositoryTest extends RepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerRecommendRepository answerRecommendRepository;
    @Test
    @DisplayName("로그인된 member의 아이디와 answer로 answerRecommend 조회")
    void findByAnswerAndMember() {
        // Given
        Member member = createMember();
        memberRepository.save(member);

        Question question = createQuestion(member);
        questionRepository.save(question);

        Answer answer = createAnswerForRecommend(member, question);
        answerRepository.save(answer);

        AnswerRecommend answerRecommend = AnswerRecommend.builder()
                .type(TypeEnum.UPVOTE)
                .member(member)
                .answer(answer)
                .build();
        answerRecommendRepository.save(answerRecommend);

        // When
        AnswerRecommend foundRecommend = answerRecommendRepository.findByAnswerAndMember(answer, member);

        // Then
        assertNotNull(foundRecommend);
        assertThat(foundRecommend.getAnswer()).isEqualTo(answer);
        assertThat(foundRecommend.getMember()).isEqualTo(member);
        assertThat(foundRecommend.getType()).isEqualTo(TypeEnum.UPVOTE);

    }
}