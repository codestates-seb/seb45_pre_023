package sixman.stackoverflow.domain.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sixman.stackoverflow.domain.answer.entitiy.Answer;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
}
