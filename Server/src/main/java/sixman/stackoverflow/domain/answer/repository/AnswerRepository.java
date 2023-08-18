package sixman.stackoverflow.domain.answer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.question.entity.Question;
@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {
    Page<Answer> findAllByQuestion(Question question, Pageable pageable);
}

