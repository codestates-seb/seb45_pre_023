package sixman.stackoverflow.domain.reply.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.answer.service.request.AnswerCreateServiceRequest;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.controller.dto.AnswerSortRequest;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.repository.ReplyRepository;
import sixman.stackoverflow.domain.reply.service.dto.request.ReplyCreateServiceRequest;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.exception.businessexception.answerexception.AnswerNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.replyexception.ReplyNotFoundException;
import sixman.stackoverflow.global.testhelper.ServiceTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ReplyServiceTest extends ServiceTest {
    @Autowired
    private ReplyService replyService;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private QuestionRepository questionRepository;


    @Test
    @DisplayName("리플의 content와 answerId를 받아 댓글을 생성한다.") // o // 0
    void createReply() {


        Member member = createMember();
        memberRepository.save(member);


        Question question = createQuestion(member);
        questionRepository.save(question);

        Answer answer = createAnswer(question);
        answerRepository.save(answer);


        setDefaultAuthentication(member.getMemberId());

        String content = "content";

        ReplyCreateServiceRequest request = new ReplyCreateServiceRequest(content);

        //when
        Long replyId = replyService.createReply(request, answer.getAnswerId());

        //then
        Reply savedReply = replyRepository.findById(replyId).orElseThrow();
        assertThat(savedReply.getReplyId()).isEqualTo(replyId);
        assertThat(savedReply.getContent()).isEqualTo(content);
        assertThat(savedReply.getAnswer().getAnswerId()).isEqualTo(answer.getAnswerId());
        assertThat(savedReply.getMember().getMemberId()).isEqualTo(member.getMemberId());
    }

    private Question createQuestion(Member member) {
        return Question.builder()
                .member(member)
                .detail("detail")
                .title("title")
                .expect("expect")
                .build();
    }

    private Answer createAnswer(Question question) {
        return Answer.builder()
                .question(question)
                .content("content")
                .build();
    }


    @Test
    @DisplayName("댓글 조회 시 존재하지 않는 replyId 이면 ReplyNotFoundException 을 반환한다.") // o
    void findReplyException() {
        // Given
        long replyId = 12345L;
        // When, Then
        assertThrows(ReplyNotFoundException.class, () -> replyService.findReply(replyId));
    }





    @Test
    @DisplayName("answerId 를 통해 댓글 목록을 페이징으로 찾아서 반환한다.(10개의 경우)") // ㅇ
    void getRepliesPaged() {
        //given
        Member member = createMember();
        memberRepository.save(member);

        Question question = createQuestion(member);
        questionRepository.save(question);

        Answer answer = createAnswer(question);
        answerRepository.save(answer);

        for (int i = 0; i < 10; i++) {
            Reply reply = Reply.builder()
                    .content("Reply Content " + i)
                    .member(member)
                    .answer(answer)
                    .build();
            replyRepository.save(reply);
        }

        Pageable pageable = PageRequest.of(0, 5);

        // When
        Page<ReplyResponse> replyResponsesPage = replyService.getRepliesPaged(answer.getAnswerId(), pageable);

        // Then
        assertThat(replyResponsesPage.getContent()).hasSize(5);
        assertThat(replyResponsesPage.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("댓글 update 시 존재하지 않는 replyId 이면 ReplyNotFoundException 을 반환한다.") // o
    void updateReplyException() {
        // Given
        Member myMember = createMember();
        memberRepository.save(myMember);
        Member member = createMember();
        memberRepository.save(member);

        Question question = createQuestion(member);
        questionRepository.save(question);

        Answer answer = createAnswer(question);
        answerRepository.save(answer);

        String oldContent = "old content";
        Reply reply = Reply.builder()
                .content(oldContent)
                .answer(answer)
                .build();
        replyRepository.save(reply);
        Long replyId = 123123434L;

        setDefaultAuthentication(myMember.getMemberId()); //myMember 로 로그인

        // When, Then
        assertThrows(ReplyNotFoundException.class, () -> replyService.updateReply(replyId, "123"));
    }

    @Test
    @DisplayName("로그인한 사용자가 아니면 업데이트를 할 수 없고, MemberNotFoundException을 반환한다.") // o
    void updateReplyMemberException() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        Question question = createquestion(member);
        questionRepository.save(question);

        Answer answer = createAnswer(question);
        answerRepository.save(answer);

        Reply reply = Reply.builder()
                .content("dasd")
                .member(member)
                .answer(answer)
                .build();
        // when, then
        assertThrows(MemberNotFoundException.class, () ->
                replyService.updateReply(reply.getReplyId(), "new content"));
    }

    @Test
    @DisplayName("answerId 를 통해 댓글 목록을 페이징으로 찾아서 반환한다.(4개의 경우)") // ㅇ
    void getRepliesPaged1() {
        //given
        Member member = createMember();
        memberRepository.save(member);

        Question question = createQuestion(member);
        questionRepository.save(question);

        Answer answer = createAnswer(question);
        answerRepository.save(answer);

        for (int i = 0; i < 4; i++) {
            Reply reply = Reply.builder()
                    .content("Reply Content " + i)
                    .member(member)
                    .answer(answer)
                    .build();
            replyRepository.save(reply);
        }

        Pageable pageable = PageRequest.of(0, 5);

        // When
        Page<ReplyResponse> replyResponsesPage = replyService.getRepliesPaged(answer.getAnswerId(), pageable);

        // Then
        assertThat(replyResponsesPage.getContent()).hasSize(4);
        assertThat(replyResponsesPage.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("replyId로 댓글 조회한다") // ㅇ
    void findReply() {
        //given
        Member member = createMember();
        memberRepository.save(member);

        Question question = createQuestion(member);
        questionRepository.save(question);

        Answer answer = createAnswer(question);
        answerRepository.save(answer);

        String oldContent = "old content";
        Reply reply = Reply.builder()
                .content(oldContent)
                .member(member)
                .answer(answer)
                .build();
        replyRepository.save(reply);

        setDefaultAuthentication(member.getMemberId());

        //when
        ReplyResponse replyResponse = replyService.findReply(reply.getReplyId());

        //then
        assertThat(replyResponse).isNotNull();
        assertThat(replyResponse.getReplyId()).isEqualTo(reply.getReplyId());
        assertThat(replyResponse.getContent()).isEqualTo(reply.getContent());
        assertThat(replyResponse.getMember().getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    @DisplayName("content를 새로운 내용으로 update한다") //ㅇ
    void updateReply() {
        //given
        Member member = createMember();
        memberRepository.save(member);

        Question question = createQuestion(member);
        questionRepository.save(question);

        Answer answer = createAnswer(question);
        answerRepository.save(answer);

        String oldContent = "old content";
        Reply reply = Reply.builder()
                .content(oldContent)
                .member(member)
                .answer(answer)
                .build();
        replyRepository.save(reply);

        setDefaultAuthentication(member.getMemberId());

        String newContent = "new Content";

        // When
        Reply updatedReply = replyService.updateReply(reply.getReplyId(), newContent);

        //then
        assertThat(updatedReply).isNotNull();
        assertThat(updatedReply.getContent()).isEqualTo(newContent);

    }


    @Test
    @DisplayName("특정 댓글을 삭제한다.") //ㅇ
    void deleteReply() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        Question question = createQuestion(member);
        questionRepository.save(question);

        Answer answer = createAnswer(question);
        answerRepository.save(answer);

        String content = "deleted content1";

        Reply reply = Reply.builder()
                .content(content)
                .member(member)
                .answer(answer)
                .build();
        replyRepository.save(reply);

        setDefaultAuthentication(member.getMemberId());

        // When
        replyService.deleteReply(reply.getReplyId());

        // Then
        Optional<Reply> deletedReply = replyRepository.findById(reply.getReplyId());
        assertThat(deletedReply).isEmpty();

    }

    @Test
    @DisplayName("존재하지 않는 댓글을 삭제하면 ReplyNotFoundException가 발생한다.") //o
    void deleteNonExistentReply() {
        // Given
        long replyId = 12345L;

        // When, Then
        assertThrows(ReplyNotFoundException.class, () -> replyService.findReply(replyId));
    }
}


















