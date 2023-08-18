package sixman.stackoverflow.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixman.stackoverflow.domain.question.service.response.QuestionTagResponse;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.tag.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String tagName);
    List<QuestionTag> findAllById(List<Integer> tagIds);
}
