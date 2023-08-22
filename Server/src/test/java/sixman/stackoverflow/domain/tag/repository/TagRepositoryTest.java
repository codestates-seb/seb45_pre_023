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

import static org.assertj.core.api.Assertions.*;

class TagRepositoryTest extends RepositoryTest {

    @Autowired TagRepository tagRepository;

    @Test
    @DisplayName("tagId 로 tag list 를 조회한다.")
    void findAllByTagIdIn() {
        //given
        Member member = createMember();

        Question question = createQuestion(member);

        Tag tag1 = createTag("tag1");
        Tag tag2 = createTag("tag2");

        QuestionTag.createQuestionTag(question, tag1);
        QuestionTag.createQuestionTag(question, tag2);

        em.persist(member);
        em.persist(question);
        em.persist(tag1);
        em.persist(tag2);
        em.flush();
        em.clear();

        //when
        List<Tag> allByTagIdIn = tagRepository.findAllByTagIdIn(List.of(tag1.getTagId(), tag2.getTagId()));

        //then
        assertThat(allByTagIdIn).hasSize(2)
                .extracting("tagId", "tagName")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(tag1.getTagId(), tag1.getTagName()),
                        Tuple.tuple(tag2.getTagId(), tag2.getTagName())
                );
    }

    @Test
    @DisplayName("tagName 리스트로 tag list 를 조회한다.")
    void findAllByTagNameIn() {
        //given
        Member member = createMember();

        Question question = createQuestion(member);

        Tag tag1 = createTag("tag1");
        Tag tag2 = createTag("tag2");

        QuestionTag.createQuestionTag(question, tag1);
        QuestionTag.createQuestionTag(question, tag2);

        em.persist(member);
        em.persist(question);
        em.persist(tag1);
        em.persist(tag2);
        em.flush();
        em.clear();

        //when
        List<Tag> allByTagIdIn = tagRepository.findAllByTagNameIn(List.of(tag1.getTagName(), tag2.getTagName()));

        //then
        assertThat(allByTagIdIn).hasSize(2)
                .extracting("tagId","tagName")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(tag1.getTagId(), tag1.getTagName()),
                        Tuple.tuple(tag2.getTagId(), tag2.getTagName())
                );

    }
}