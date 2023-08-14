package sixman.stackoverflow.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixman.stackoverflow.domain.tag.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String tagName);
}
