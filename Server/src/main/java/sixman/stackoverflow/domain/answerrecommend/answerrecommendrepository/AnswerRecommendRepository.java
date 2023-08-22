package sixman.stackoverflow.domain.answerrecommend.answerrecommendrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answerrecommend.entity.AnswerRecommend;
import sixman.stackoverflow.domain.member.entity.Member;

import java.util.Optional;


public interface AnswerRecommendRepository extends JpaRepository<AnswerRecommend,Long> {

    AnswerRecommend findByAnswerAndMember(Answer answer, Member member);

    Optional<AnswerRecommend> findByMemberAndAnswer(Member member, Answer answer);
}
