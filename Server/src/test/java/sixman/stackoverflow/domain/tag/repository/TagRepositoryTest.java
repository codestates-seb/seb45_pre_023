package sixman.stackoverflow.domain.tag.repository;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.testhelper.RepositoryTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class TagRepositoryTest extends RepositoryTest {

    @Autowired TagRepository tagRepository;

    @Test
    @DisplayName("tagId 로 tag list 를 조회한다.")
    void findAllByTagIdIn() {
        //given
        Tag tag1 = createTag("tag1");
        Tag tag2 = createTag("tag2");

        em.persist(tag1);
        em.persist(tag2);
        em.flush();
        em.clear();

        //when
        List<Tag> allByTagIdIn = tagRepository.findAllByTagIdIn(List.of(tag1.getTagId(), tag2.getTagId()));

        //then
        assertThat(allByTagIdIn).hasSize(2)
                .extracting("tagId", "tagName", "tagDetail")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(tag1.getTagId(), tag1.getTagName(), tag1.getTagDetail()),
                        Tuple.tuple(tag2.getTagId(), tag2.getTagName(), tag2.getTagDetail())
                );
    }

    @Test
    @DisplayName("tagName 리스트로 tag list 를 조회한다.")
    void findAllByTagNameIn() {
        //given
        Tag tag1 = createTag("tag1");
        Tag tag2 = createTag("tag2");

        em.persist(tag1);
        em.persist(tag2);
        em.flush();
        em.clear();

        //when
        List<Tag> allByTagIdIn = tagRepository.findAllByTagNameIn(List.of(tag1.getTagName(), tag2.getTagName()));

        //then
        assertThat(allByTagIdIn).hasSize(2)
                .extracting("tagId", "tagName", "tagDetail")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(tag1.getTagId(), tag1.getTagName(), tag1.getTagDetail()),
                        Tuple.tuple(tag2.getTagId(), tag2.getTagName(), tag2.getTagDetail())
                );
    }

    @Test
    @DisplayName("tagName 으로 tag 를 조회한다.")
    void findByTagName() {
        //given
        Tag tag1 = createTag("tag1");

        em.persist(tag1);
        em.flush();
        em.clear();

        //when
        Optional<Tag> findTag = tagRepository.findByTagName(tag1.getTagName());

        //then
        assertThat(findTag).isPresent()
                .get()
                .extracting("tagId", "tagName", "tagDetail")
                .containsExactly(tag1.getTagId(), tag1.getTagName(), tag1.getTagDetail());

    }
}