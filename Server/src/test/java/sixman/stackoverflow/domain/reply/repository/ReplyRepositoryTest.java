package sixman.stackoverflow.domain.reply.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.global.testhelper.RepositoryTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class ReplyRepositoryTest extends RepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("answerId에 관련된 모든 reply을 page로 받아온다")
    void findAllRepliesByAnswer() {

        // Given
        Member member = createMember();
        memberRepository.save(member);

        Question question = createQuestion(member);
        questionRepository.save(question);

        Answer answer = createAnswer(question);
        answerRepository.save(answer);

        // 주어진 Answer와 연관된 댓글 생성
        List<Reply> replies = new ArrayList<>();
        replies.add(createReply(answer, "댓글 1"));
        replies.add(createReply(answer, "댓글 2"));
        replies.add(createReply(answer, "댓글 3"));
        replyRepository.saveAll(replies);

        Pageable pageable = PageRequest.of(0, 10); // 페이지 0, 페이지 당 최대 10개 아이템

        // When
        Page<Reply> replyPage = replyRepository.findByAnswer(answer, pageable);

        // Then
        assertThat(replyPage.getContent()).hasSize(replies.size());

    }

    private Answer createAnswer(Question question) {
        return Answer.builder()
                .content("dasdasd")
                .question(question)
                .build();
    }

    private Reply createReply(Answer answer, String content) {
        return Reply.builder()
                .content(content)
                .answer(answer)
                .build();
    }
}