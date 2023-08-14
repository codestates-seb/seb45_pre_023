package sixman.stackoverflow.domain.questionrecommend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.global.entity.TypeEnum;

public interface QuestionRecommendRepository extends JpaRepository<QuestionRecommend, Long> {

    long countByQuestionAndType(Question question, TypeEnum type);
}

