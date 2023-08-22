package sixman.stackoverflow.domain.answer.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.global.testhelper.RepositoryTest;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerRepositoryTest extends RepositoryTest {

    @Autowired AnswerRepository answerRepository;

    @Test
    @DisplayName("Question을 받아 해당 글의 답변을 페이징해서 조회한다.")
    void findAllByQuestion(){
        // given
        Member member = createMember();
        em.persist(member);

        Question question = createQuestion(member);
        em.persist(question);

        for (int i = 1; i <= 20; i++) {
            Answer answer = createAnswerForRecommend(member,question);
            em.persist(answer);
        }
        em.flush();
        em.clear();

        PageRequest pageable = PageRequest.of(0, 5);

        // when
        Page<Answer> answerPage = answerRepository.findAllByQuestion(question, pageable);

        // then
        assertThat(answerPage.getContent()).hasSize(5);
        assertThat(answerPage.getTotalElements()).isEqualTo(20);
        assertThat(answerPage.getNumber()).isEqualTo(0);
        assertThat(answerPage.getTotalPages()).isEqualTo(4);
    }
}

