package sixman.stackoverflow.domain.reply.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.repository.ReplyRepository;
import sixman.stackoverflow.domain.reply.service.dto.request.ReplyCreateServiceRequest;
import sixman.stackoverflow.global.exception.businessexception.answerexception.AnswerNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.replyexception.ReplyNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("local")
class ReplyServiceTest {
    @InjectMocks
    private ReplyService replyService;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Mock
    private ReplyRepository replyRepository;



    @Test
    @DisplayName("리플의 content와 answerId를 받아 댓글을 생성한다.")
    void createReply() {


        //given
        String content = "testtest";
        Member member = Member.builder()
                .memberId(3L)
                .build();
        Answer answer = Answer.builder()
                .answerId(4L)
                .build();
        ReplyCreateServiceRequest request = ReplyCreateServiceRequest.builder()
                .content(content)
                .build();

//        ReplyRepository replyRepository = mock(ReplyRepository.class); // 이렇게 안하고 위에서 @mock
//        ReplyService replyService = new ReplyService(replyRepository); //

//        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
//        when(answerRepository.findById(answerId)).thenReturn(Optional.of(answer));
//        when(replyRepository.save(any(Reply.class))).thenReturn(Reply.builder().replyId(replyId).build());
//
//        Long answerId = 4L;
//        // When
//        Long replyId = replyService.createReply(request, answerId);
//
//        // Then
//        assertEquals(content, reply.getContent());
//        assertEquals(member.getMemberId(), reply.getMember().getMemberId());
//        assertEquals(answer.getAnswerId(), reply.getAnswer().getAnswerId());
    }




    @Test
    @DisplayName("댓글 조회 시 존재하지 않는 replyId 이면 AnswerNotFoundException 을 반환한다.")
    void findReplyException() {

        // Given
        long nonExistentAnswerId = 586L;

        when(answerRepository.findById(nonExistentAnswerId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(AnswerNotFoundException.class, () -> answerRepository.findById(nonExistentAnswerId));
    }

    @Test
    @DisplayName("")
    void getRepliesPaged() {
    }

    @Test
    void findReply() {
    }

    @Test
    void updateReply() {
    }

    @Test
    @DisplayName("특정 댓글을 삭제한다.")
    void deleteReply() {
        // Given
        long replyId = 1L;
        long memberId = 2L;

        Reply reply = Reply.builder()
                .replyId(replyId)
                .member(Member.builder().memberId(memberId).build())
                .build();

        when(replyRepository.findById(replyId)).thenReturn(Optional.of(reply));

        // When
        replyService.deleteReply(replyId);

//        // Then
//        verify(replyRepository).findById(replyId);
//        verify(replyRepository).deleteById(replyId);
    }

    @Test
    @DisplayName("존재하지 않는 댓글을 삭제하면 예외가 발생한다.")
    void deleteNonExistentReply() {
        // Given
        long nonExistentReplyId = 999L;

        when(replyRepository.findById(nonExistentReplyId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(ReplyNotFoundException.class, () -> replyService.deleteReply(nonExistentReplyId));
    }
}