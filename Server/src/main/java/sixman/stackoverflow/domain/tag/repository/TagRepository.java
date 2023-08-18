package sixman.stackoverflow.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.tag.entity.Tag;

import java.util.List;



public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByTagIdIn(List<Long> tagIds);
}
