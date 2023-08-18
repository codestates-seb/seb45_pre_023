package sixman.stackoverflow.domain.question.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sixman.stackoverflow.domain.question.entity.Question;

import java.util.Optional;
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findAll(Pageable pageable);
    Optional<Question> findByQuestionId(Long questionId);

    default Long findMemberIdByQuestionId(Long questionId) {
        return findByQuestionId(questionId)
                .map(question -> question.getMember().getMemberId())
                .orElse(null);
    }
}
