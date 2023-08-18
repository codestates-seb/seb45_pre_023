package sixman.stackoverflow.domain.questiontag;

import org.springframework.data.jpa.repository.JpaRepository;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;

public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {
}