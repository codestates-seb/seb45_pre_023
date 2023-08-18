package sixman.stackoverflow.domain.tag.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.domain.tag.repository.TagRepository;
import sixman.stackoverflow.global.testhelper.ServiceTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class TagServiceTest extends ServiceTest {

    @Autowired TagRepository tagRepository;
    @Autowired TagService tagService;

    @Test
    @DisplayName("모든 태그를 조회한다.")
    void getTags() {
        //given
        Tag tag1 = createTag("tag1");
        Tag tag2 = createTag("tag2");
        Tag tag3 = createTag("tag3");

        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);

        //when
        tagService.getTags();

        //then
        assertThat(tagService.getTags()).hasSize(3)
                .extracting("tagName")
                .containsExactlyInAnyOrder("tag1", "tag2", "tag3");
    }
}