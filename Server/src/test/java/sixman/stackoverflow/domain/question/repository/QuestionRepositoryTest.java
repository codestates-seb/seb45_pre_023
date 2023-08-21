package sixman.stackoverflow.domain.question.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.testhelper.RepositoryTest;

import javax.persistence.TableGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QuestionRepositoryTest extends RepositoryTest {

    @Autowired QuestionRepository questionRepository;

    @Test
    @DisplayName("tagName 으로 Question 을 페이징해서 조회한다.")
    void findAllByTag() {
        // given
        Member member = createMember();
        em.persist(member);

        Tag tag1 = createTag("tag1");
        Tag tag2 = createTag("tag2");
        em.persist(tag1);
        em.persist(tag2);

        for (int i = 1; i <= 40; i++) {
            Question question = createQuestion(member);
            em.persist(question);
            Tag tag = i % 2 == 0 ? tag1 : tag2;
            QuestionTag questionTag = QuestionTag.createQuestionTag(question, tag);
            em.persist(questionTag);
        }
        em.flush();
        em.clear();

        Pageable pageable = Pageable.ofSize(10).withPage(0);

        // when
        Page<Question> questionPage = questionRepository.findAllByTag(pageable, tag1.getTagName());

        // then
        Assertions.assertThat(questionPage.getContent()).hasSize(10);
        questionPage.getContent().forEach(question
                -> assertThat(question.getQuestionTags().stream()
                .map(QuestionTag::getTag)
                .map(Tag::getTagName))
                .contains(tag1.getTagName()));
    }

    @Test
    @DisplayName("questionId으로 해당 질문글의 memberId를 조회한다.")
    void findMemberIdByQuestionId(){
        // given
        Member member = createMember();
        em.persist(member);

        Question question = createQuestion(member);
        em.persist(question);
        em.flush();
        em.clear();

        // when
        Long memberId = questionRepository.findMemberIdByQuestionId(question.getQuestionId());

        // then
        assertThat(memberId).isEqualTo(member.getMemberId());
    }
}