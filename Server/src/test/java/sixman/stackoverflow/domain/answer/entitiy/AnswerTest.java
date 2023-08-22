package sixman.stackoverflow.domain.answer.entitiy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.reply.entity.Reply;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AnswerTest {

    @Test
    @DisplayName("memberId, questionId 답변내용을 받아 답변 객체를 생성한다.")
    void createAnswers() {

        //given
        String content = "test content";
        Long memberId = 1L;

        Member member = Member.builder()
                .memberId(memberId)
                .build();
        Question question = Question.builder()
                .member(member)
                .detail("asd")
                .title("asdasd")
                .expect("vbbbb")
                .build();

        // When, Then
        Answer answer = Answer.createAnswers(content, member, question);
        assertThat(answer.getContent()).isEqualTo(content);
        assertThat(answer.getMember().getMemberId()).isEqualTo(memberId);
        assertThat(answer.getQuestion().getQuestionId()).isEqualTo(question.getQuestionId());
    }

}
