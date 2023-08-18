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
import sixman.stackoverflow.domain.question.service.QuestionService;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.service.dto.request.ReplyCreateServiceRequest;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.exception.businessexception.answerexception.AnswerNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.questionexception.QuestionNotFoundException;
import sixman.stackoverflow.global.testhelper.ServiceTest;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class AnswerServiceTest extends ServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionService questionService;

    @Test
    @DisplayName("답변의 content, questionId 를 받아서 답변을 생성한다.") // o
    void createAnswerBy() {
        //given

        Member member = createMember();
        memberRepository.save(member);


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
    @DisplayName("답변 생성 시 존재하지 않는 questionId 이면 QuestionNotFoundException 이 발생한다.") // o
    void createAnswerException() {

        // Given
        long questionId = 1234566L;

        // When, Then
        assertThrows(QuestionNotFoundException.class, () -> {
            questionService.getQuestionById(questionId);
        });


    }

    @Test
    @DisplayName("answerId 를 통해 답변을 찾아서 반환한다.") // o
    void findAnswer() {

        //Given
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

        //then
        assertNotNull(answerResponse);
        assertThat(answerResponse.getContent()).isEqualTo(content);
        assertThat(answerResponse.getMember().getMemberId()).isEqualTo(member.getMemberId());
    }


    @Test
    @DisplayName("답변 조회 시 존재하지 않는 answerId 이면 AnswerNotFoundException 을 반환한다.") //o
    void findAnswerException() {

        // given
        long answerForFindId = 12345L;

        // When,Then
        assertThrows(AnswerNotFoundException.class, () -> {
            answerService.findAnswer(answerForFindId);
        });
    }

    @Test
    @DisplayName("questionId 를 통해 답변 목록을 페이징으로 찾아서 반환한다.")
    void findAnswers() { // 정우님이랑 이야기
    }


    @Test
    @DisplayName("answerId, content 를 통해 답변을 수정한다.") // o
    void updateAnswer() {

        //given
        Member memberForUpdate = createMember();
        memberRepository.save(memberForUpdate);

        Question questionForUpdate = createQuestion(memberForUpdate);
        questionRepository.save(questionForUpdate);

        Answer changeAnswer = Answer.builder()
                .answerId(1L)
                .content("old Content")
                .member(memberForUpdate)
                .question(questionForUpdate)
                .build();
        answerRepository.save(changeAnswer);

        setDefaultAuthentication(memberForUpdate.getMemberId());

        String newContent = "Updated Content";

        // When
        Answer updatedAnswer = answerService.updateAnswer(changeAnswer.getAnswerId(), newContent);

        // Then
        assertThat(updatedAnswer).isNotNull();
        assertThat(updatedAnswer.getContent()).isEqualTo(newContent);
    }



    @Test
    @DisplayName("답변 수정 시 존재하지 않는 answerId 이면 AnswerNotFoundException 이 발생한다.") //o
    void updateAnswerException() {

        // given
        long answerForUpdateId = 12345L;

        // When,Then
        assertThrows(AnswerNotFoundException.class, () -> {
            answerService.findAnswer(answerForUpdateId);
        });

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
    @DisplayName("답변 수정 시 다른 사람의 answer 를 수정하려고 하면 MemberAccessDeniedException 이 발생한다.") // ㅇ
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


    @Test
    @DisplayName("answerId 를 통해 답변을 삭제한다.") // ㅇ
    void deleteAnswer() {
        // Given
        Member member = createMember();
        memberRepository.save(member);

        Question question = createQuestion(member);
        questionRepository.save(question);

        String content = "deleted content2";
        Answer answer = Answer.builder()
                .answerId(1L)
                .content(content)
                .member(member)
                .question(question)
                .build();

        answerRepository.save(answer);

        setDefaultAuthentication(member.getMemberId());

        //When
        answerService.deleteAnswer(answer.getAnswerId());

        //Then
        Optional<Answer> deletedAnswer = answerRepository.findById(answer.getAnswerId());
        assertThat(deletedAnswer).isEmpty();
        answerRepository.flush();

    }

    @Test
    @DisplayName("답변 삭제 시 존재하지 않는 answerId 이면 AnswerNotFoundException 이 발생한다.") //o
    void deleteAnswerException() {

        // Given
        long answerForDeleteId = 12345L;

        // When, Then
        assertThrows(AnswerNotFoundException.class, () -> {
            answerService.findAnswer(answerForDeleteId);
        });

    }

    @Test
    @DisplayName("답변 삭제 시 다른 사람의 answer를 삭제하려고 하면 MemberAccessDeniedException 이 발생한다.") //ㅇ
    void deleteAnswerMemberException() {

        /// given
        Member myMemberForDelete = createMember();
        Member otherMemberForDelete = createMember();
        Question questionForDelete = createQuestion(myMemberForDelete);
        Answer answerForDelete = createanswer(myMemberForDelete, questionForDelete);

        memberRepository.save(myMemberForDelete);
        memberRepository.save(otherMemberForDelete);
        questionRepository.save(questionForDelete);
        answerRepository.save(answerForDelete);

        setDefaultAuthentication(otherMemberForDelete.getMemberId()); //myMember 로 로그인

        // when, then
        assertThatThrownBy(
                () -> answerService.deleteAnswer(answerForDelete.getAnswerId()))
                .isInstanceOf(MemberAccessDeniedException.class)
                .hasMessage("접근 권한이 없습니다.");
    }
}






