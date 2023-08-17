package sixman.stackoverflow.domain.questionrecommend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.global.entity.TypeEnum;

import java.util.Optional;

public interface QuestionRecommendRepository extends JpaRepository<QuestionRecommend, Long> {
    Optional<QuestionRecommend> findByMemberAndQuestionAndType(Member member, Question question, TypeEnum type);
}

