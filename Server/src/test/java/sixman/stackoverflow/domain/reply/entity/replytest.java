package sixman.stackoverflow.domain.reply.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.member.entity.Member;
import static org.assertj.core.api.Assertions.*;

class replytest { //passed 너무 오래걸림 간단하게 짰어야 했다

    @Test
    @DisplayName("memberId, answerId, 리플내용을 받아 리플 객체를 생성한다.")
    void createReply() {


    //given
    String content = "test content";
    Long memberId = 1L;
    Long answerId = 2L;
    Member member = Member.builder()
            .memberId(memberId)
            .build();
    Answer answer = Answer.builder()
            .answerId(answerId)
            .build();

    // when

    Reply reply = Reply.createReply(content, answer, member);
        assertThat(reply.getContent()).isEqualTo(content);
        assertThat(reply.getMember().getMemberId()).isEqualTo(memberId);
        assertThat(reply.getAnswer().getAnswerId()).isEqualTo(answerId);


}
}
