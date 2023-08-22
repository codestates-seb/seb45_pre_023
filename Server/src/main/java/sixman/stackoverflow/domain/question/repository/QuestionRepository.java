package sixman.stackoverflow.domain.question.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sixman.stackoverflow.domain.question.entity.Question;

import java.util.Optional;
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findAll(Pageable pageable);
    Optional<Question> findByQuestionId(Long questionId);

    @Query("SELECT q FROM Question q JOIN q.questionTags qt JOIN qt.tag t WHERE t.tagName = :tag")
    Page<Question> findAllByTag(Pageable pageable, @Param("tag") String tag);

    default Long findMemberIdByQuestionId(Long questionId) {
        return findByQuestionId(questionId)
                .map(question -> question.getMember().getMemberId())
                .orElse(null);
    }
}
