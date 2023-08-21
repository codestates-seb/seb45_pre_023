package sixman.stackoverflow.domain.answer.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.answer.service.request.AnswerCreateServiceRequest;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.controller.dto.AnswerSortRequest;
import sixman.stackoverflow.domain.question.controller.dto.QuestionSortRequest;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
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

        Answer answer = createanswer(member, question);
        answerRepository.save(answer);

        setDefaultAuthentication(member.getMemberId());

        //when
        AnswerResponse answerResponse = answerService.findAnswer(answer.getAnswerId());

        //then
        assertThat(answerResponse.getContent()).isEqualTo(answer.getContent());
        assertThat(answerResponse.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(answerResponse.getAnswerId()).isEqualTo(answer.getAnswerId());
    }


    @Test
    @DisplayName("답변 조회 시 존재하지 않는 answerId 이면 AnswerNotFoundException 을 반환한다.") //o
    void findAnswerException() {

        // given
        Long answerId = null;

        // When,Then
        assertThrows(AnswerNotFoundException.class, () ->
            answerService.findAnswer(answerId));

    }

    @Test
    @DisplayName("questionId 를 통해 답변 목록을 페이징으로 찾아서 최신순으로 반환한다.")
    void findAnswersBySortCreatedDate() {
        // given
        Member member = createMember();
        Question question = createQuestion(member);

        for(int i=0; i<5; i++) {
            Answer answer = createanswerdetail(member, question,i);
            answerRepository.save(answer);
        }

        memberRepository.save(member);
        questionRepository.save(question);

        Pageable pageable = PageRequest.of(0, 5, Sort.by(AnswerSortRequest.CREATED_DATE.getValue()).descending());

        //when
        Page<AnswerResponse> result = answerService.findAnswers(question.getQuestionId(), pageable);

        // then
        assertThat(result.getContent()).hasSize(5);

        List<AnswerResponse> answerList = result.getContent();
        Instant previousCreatedDate = Instant.now();

        for (AnswerResponse answer : answerList) {
            LocalDateTime currentLocalDateTime = answer.getCreatedDate();
            Instant currentCreatedDate = currentLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();

            assertThat(currentCreatedDate).isBeforeOrEqualTo(previousCreatedDate);
            previousCreatedDate = currentCreatedDate;
        }
    }

    @Test
    @DisplayName("questionId 를 통해 답변 목록을 페이징으로 찾아서 추천수 순서로 반환한다.")
    void findAnswersBySortRecommend() {
        // given
        Member member = createMember();
        Question question = createQuestion(member);

        for(int i=0; i<5; i++) {
            Answer answer = createanswerdetail(member, question,i);
            answerRepository.save(answer);
        }

        memberRepository.save(member);
        questionRepository.save(question);

        Pageable pageable2 = PageRequest.of(0, 5, Sort.by(AnswerSortRequest.RECOMMEND.getValue()).descending());

        //when
        Page<AnswerResponse> result2 = answerService.findAnswers(question.getQuestionId(), pageable2);

        // then
        assertThat(result2.getContent()).hasSize(5);

        List<AnswerResponse> answerList = result2.getContent();
        int previousRecommend = Integer.MAX_VALUE;

        for (AnswerResponse answer : answerList) {
            assertThat(answer.getRecommend()).isLessThanOrEqualTo(previousRecommend);
            previousRecommend = answer.getRecommend();
        }

    }


    @Test
    @DisplayName("answerId, content 를 통해 답변을 수정한다.")
    void updateAnswer() {

        //given
        Member member = createMember();
        memberRepository.save(member);

        Question question = createQuestion(member);
        questionRepository.save(question);

        Answer answer = createanswer(member, question);
        answerRepository.save(answer);


        setDefaultAuthentication(member.getMemberId());

        String newContent = "Updated Content";

        // When
        Answer updatedAnswer = answerService.updateAnswer(answer.getAnswerId(), newContent);

        // Then
        assertThat(updatedAnswer).isNotNull();
        assertThat(updatedAnswer.getContent()).isEqualTo(newContent);
        assertThat(updatedAnswer.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(updatedAnswer.getAnswerId()).isEqualTo(answer.getAnswerId());
    }



    @Test
    @DisplayName("답변 수정 시 존재하지 않는 answerId 이면 AnswerNotFoundException 이 발생한다.") //o
    void updateAnswerException() {

        // given
        Long answerForUpdateId = null;

        // When,Then
        assertThrows(AnswerNotFoundException.class, () -> {
            answerService.findAnswer(answerForUpdateId);
        });
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
                .content(content)
                .member(member)
                .question(question)
                .build();

        answerRepository.save(answer);

        setDefaultAuthentication(member.getMemberId());

        //When
        answerService.deleteAnswer(answer.getAnswerId());

        //Then
        boolean answerExists = answerRepository.existsById(answer.getAnswerId());
        org.junit.jupiter.api.Assertions.assertFalse(answerExists, "답변이 삭제되었으므로 해당 answerId의 답변이 더 이상 존재해서는 안됩니다.");





    }

    @Test
    @DisplayName("답변 삭제 시 존재하지 않는 answerId 이면 AnswerNotFoundException 이 발생한다.") //o ////
    void deleteAnswerException() {

        // Given
        Long answerForDeleteId = null;

        // When, Then
        assertThrows(AnswerNotFoundException.class, () -> {
            answerService.findAnswer(answerForDeleteId);
        });

    }

    @Test
    @DisplayName("답변 삭제 시 다른 사람의 answer를 삭제하려고 하면 MemberAccessDeniedException 이 발생한다.") //ㅇ //
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






