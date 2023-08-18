package sixman.stackoverflow.domain.answer.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.answer.service.request.AnswerCreateServiceRequest;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.service.dto.request.ReplyCreateServiceRequest;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.exception.businessexception.answerexception.AnswerNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.testhelper.ServiceTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AnswerServiceTest extends ServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("답변의 content, questionId 를 받아서 답변을 생성한다.")
    void createAnswer() {
        //given
        //멤버 저장
        Member member = createMember();
        memberRepository.save(member);

        //질문 저장
        Question question = createQuestion(member);
        questionRepository.save(question);




        setDefaultAuthentication(member.getMemberId());

        String content = "content";

        AnswerCreateServiceRequest request = new AnswerCreateServiceRequest(content);

        //when
        Long answerId = answerService.createAnswer(request, question.getQuestionId());

        //then
        Answer savedAnswer = answerRepository.findById(answerId).orElseThrow();
        assertThat(savedAnswer.getAnswerId()).isEqualTo(answerId);
        assertThat(savedAnswer.getContent()).isEqualTo(content);
        assertThat(savedAnswer.getQuestion().getQuestionId()).isEqualTo(question.getQuestionId());
        assertThat(savedAnswer.getMember().getMemberId()).isEqualTo(member.getMemberId());
    }

    private Question createQuestion(Member member) {
        return Question.builder()
                .member(member)
                .detail("detail")
                .title("title")
                .expect("expect")
                .build();
    }

    @Test
    @DisplayName("답변 생성 시 존재하지 않는 questionId 이면 QuestionNotFoundException 이 발생한다.")
    void createAnswerException() {


    }

    @Test
    @DisplayName("answerId 를 통해 답변을 찾아서 반환한다.")
    void findAnswer() {
        Member member = createMember();
        memberRepository.save(member);

        Question question = createQuestion(member);
        questionRepository.save(question);



        setDefaultAuthentication(member.getMemberId());

        String content = "content";

        AnswerCreateServiceRequest request = new AnswerCreateServiceRequest(content);


        Long answerId = answerService.createAnswer(request, question.getQuestionId());

        //when
        AnswerResponse answerResponse = answerService.findAnswer(answerId);

        assertNotNull(answerResponse);
        assertEquals(content, answerResponse.getContent());
        assertEquals(member.getMemberId(), answerResponse.getMember().getMemberId());
    }

    @Test
    @DisplayName("답변 조회 시 존재하지 않는 answerId 이면 AnswerNotFoundException 을 반환한다.")
    void findAnswerException() {
    }

    @Test
    @DisplayName("questionId 를 통해 답변 목록을 페이징으로 찾아서 반환한다.")
    void findAnswers() {
    }

//    @Test
//    @DisplayName("answerId, content 를 통해 답변을 수정한다.")
//    void updateAnswer() {
//
//        Member member = createMember();
//        memberRepository.save(member);
//
//        Question question = createQuestion(member);
//        questionRepository.save(question);
//
//        Answer changeAnswer = Answer.builder()
//                .answerId(1L)
//                .content("old Content")
//                .member(member)
//                .question(question)
//                .build();
//        answerRepository.save(changeAnswer);
//
//        setDefaultAuthentication(member.getMemberId());
//
//        String newContent = "Updated Content";
//
//        // When
//        Answer updatedAnswer = answerService.updateAnswer(changeAnswer.getAnswerId(), newContent);
//
//        // Then
//        assertNotNull(updatedAnswer);
//        assertEquals(newContent, updatedAnswer.getContent());
//    }

    @Test
    @DisplayName("답변 수정 시 존재하지 않는 answerId 이면 AnswerNotFoundException 이 발생한다.")
    void updateAnswerException() {

//        Member member = createMember();
//        memberRepository.save(member);
//
//        Question question = createQuestion(member);
//        questionRepository.save(question);
//
//        Answer existingAnswer = Answer.builder()
//                .answerId(1L)
//                .content("old Content11")
//                .member(member)
//                .question(question)
//                .build();
//        answerRepository.save(existingAnswer);
//
//        setDefaultAuthentication(member.getMemberId());
//
//        String newContent = "Updated Content11";
//
//        // When
//        Answer updatedAnswer = answerService.updateAnswer(existingAnswer.getAnswerId(), newContent);
//
//        assertThrows(AnswerNotFoundException.class, () -> {
//            answerService.updateAnswer();
//        });


    }

    @Test
    @DisplayName("답변 수정 시 다른 사람의 answer 를 수정하려고 하면 MemberAccessDeniedException 이 발생한다.")
    void updateAnswerMemberException() {
        //given
        Member myMember = createMember();
        Member otherMember = createMember();
        Question question = createQuestion(otherMember);
        Answer answer = createanswer(otherMember, question);

        memberRepository.save(myMember);
        memberRepository.save(otherMember);
        questionRepository.save(question);
        answerRepository.save(answer);

        setDefaultAuthentication(myMember.getMemberId()); //myMember 로 로그인

        //when
        assertThatThrownBy(
                () -> answerService.updateAnswer(answer.getAnswerId(), "new content"))
                .isInstanceOf(MemberAccessDeniedException.class)
                .hasMessage("접근 권한이 없습니다.");
    }





//    @Test
//    @DisplayName("answerId 를 통해 답변을 삭제한다.")
//    void deleteAnswer() {
//        Member member = createMember();
//        memberRepository.save(member);
//
//        Question question = createQuestion(member);
//        questionRepository.save(question);
//
//        String content = "deleted content2";
//        Answer answer = Answer.builder()
//                .answerId(1L)
//                .content(content)
//                .member(member)
//                .question(question)
//                .build();
//
//        answerRepository.save(answer);
//
//        setDefaultAuthentication(member.getMemberId());
//
//        answerService.deleteAnswer(answer.getAnswerId());
//
//        Optional<Answer> deletedAnswer = answerRepository.findById(answer.getAnswerId());
//        assertFalse(deletedAnswer.isPresent());
//
//
//
//    }

    @Test
    @DisplayName("답변 삭제 시 존재하지 않는 answerId 이면 AnswerNotFoundException 이 발생한다.")
    void deleteAnswerException() {
        long answerId = 12345L;

        assertThrows(AnswerNotFoundException.class, () -> {
            answerService.findAnswer(answerId);
        });

    }

    @Test
    @DisplayName("답변 삭제 시 다른 사람의 answer 를 삭제하려고 하면 MemberAccessDeniedException 이 발생한다.")
    void deleteAnswerMemberException() {

    }
}


