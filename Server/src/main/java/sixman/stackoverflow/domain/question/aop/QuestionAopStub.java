package sixman.stackoverflow.domain.question.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.question.service.response.QuestionDetailResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionTagResponse;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;
import sixman.stackoverflow.global.response.PageInfo;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Component
@Aspect
public class QuestionAopStub {

    @Around("execution(* sixman.stackoverflow.domain.question.controller.QuestionController.getQuestions(..))")
    public Object getQuestions(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        int page = (int) args[0];

        QuestionTagResponse tag1 = QuestionTagResponse.builder()
                .questionTagId(1L)
                .tagName("tag1")
                .build();

        QuestionTagResponse tag2 = QuestionTagResponse.builder()
                .questionTagId(2L)
                .tagName("tag2")
                .build();

        List<QuestionResponse> responses = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            QuestionResponse response = QuestionResponse.builder()
                    .questionId((long) i)
                    .title("title")
                    .detail("detail")
                    .answerCount(5)
                    .member(MemberInfo.of(createMember(1L)))
                    .views(100)
                    .recommend(10)
                    .tags(List.of(tag1, tag2))
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            responses.add(response);
        }

        Page<QuestionResponse> questionResponses = new PageImpl<>(responses, PageRequest.of(page - 1, 10), 100);

        return ResponseEntity.ok(ApiPageResponse.ok(questionResponses, "질문 목록 조회 성공"));
    }

    @Around("execution(* sixman.stackoverflow.domain.question.controller.QuestionController.getQuestionById(..))")
    public Object getQuestionById(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        Long questionId = (Long) args[0];

        QuestionTagResponse tag1 = QuestionTagResponse.builder()
                .questionTagId(1L)
                .tagName("tag1")
                .build();

        QuestionTagResponse tag2 = QuestionTagResponse.builder()
                .questionTagId(2L)
                .tagName("tag2")
                .build();

        QuestionDetailResponse response = QuestionDetailResponse.builder()
                .questionId(questionId)
                .title("title")
                .detail("detail")
                .expect("expect")
                .member(MemberInfo.of(createMember(1L)))
                .views(100)
                .recommend(10)
                .recommendType(TypeEnum.UPVOTE)
                .tags(List.of(tag1, tag2))
                .answer(createAnswerResponse())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(ApiSingleResponse.ok(response, "질문 조회 성공"));
    }

    @Around("execution(* sixman.stackoverflow.domain.question.controller.QuestionController.createQuestion(..))")
    public Object createQuestion(ProceedingJoinPoint joinPoint) {


        URI uri = URI.create("/questions/1");

        return ResponseEntity.created(uri).build();
    }

    @Around("execution(* sixman.stackoverflow.domain.question.controller.QuestionController.recommendQuestion(..))")
    public Object recommendQuestion(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    @Around("execution(* sixman.stackoverflow.domain.question.controller.QuestionController.disrecommendQuestion(..))")
    public Object disrecommendQuestion(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    @Around("execution(* sixman.stackoverflow.domain.question.controller.QuestionController.updateQuestion(..))")
    public Object updateQuestion(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    @Around("execution(* sixman.stackoverflow.domain.question.controller.QuestionController.deleteQuestion(..))")
    public Object deleteQuestion(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    @Around("execution(* sixman.stackoverflow.domain.question.controller.QuestionController.createAnswer(..))")
    public Object createAnswer(ProceedingJoinPoint joinPoint) {

        URI uri = URI.create("/answers/1");

        return ResponseEntity.created(uri).build();
    }

    @Around("execution(* sixman.stackoverflow.domain.question.controller.QuestionController.getAnswers(..))")
    public Object getAnswers(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        Long questionId = (Long) args[0];
        Integer page = (Integer) args[1];

        List<AnswerResponse> answers = createAnswers();
        PageRequest pageRequest = PageRequest.of(page - 1, 5);

        Page<AnswerResponse> answerResponses = new PageImpl<>(answers, pageRequest, 20);

        return ResponseEntity.ok(ApiPageResponse.ok(answerResponses));
    }

    protected Member createMember(Long memberId) {
        return Member.builder()
                .memberId(memberId)
                .email("test@google.com")
                .nickname("test")
                .password("1234abcd!")
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().image("test url").build())
                .enabled(true)
                .build();
    }


    protected QuestionDetailResponse.QuestionAnswer createAnswerResponse() {

        List<AnswerResponse> answers = createAnswers();

        Page<AnswerResponse> page = new PageImpl<>(answers, PageRequest.of(0, 5), 20);
        PageInfo pageInfo = PageInfo.of(page);

        return QuestionDetailResponse.QuestionAnswer.builder()
                .answers(answers)
                .pageInfo(pageInfo)
                .build();
    }

    protected List<AnswerResponse> createAnswers() {
        List<AnswerResponse> answers = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {

            TypeEnum type;
            if(i % 2 == 0) {
                type = TypeEnum.UPVOTE;
            } else {
                type = TypeEnum.DOWNVOTE;
            }

            AnswerResponse answer = AnswerResponse.builder()
                    .answerId((long) i)
                    .content("content")
                    .member(MemberInfo.of(createMember((long) i)))
                    .recommend(10)
                    .recommendType(type)
                    .reply(createReplyResponse(i))
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            answers.add(answer);
        }
        return answers;
    }

    protected AnswerResponse.AnswerReply createReplyResponse(int index) {

        List<ReplyResponse> replies = getReplyResponses((long) index);

        Page<ReplyResponse> page = new PageImpl<>(replies, PageRequest.of(0, 5), 20);
        PageInfo pageInfo = PageInfo.of(page);

        return AnswerResponse.AnswerReply.builder()
                .replies(replies)
                .pageInfo(pageInfo)
                .build();
    }

    protected List<ReplyResponse> getReplyResponses(long index) {
        List<ReplyResponse> replies = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {

            ReplyResponse reply = ReplyResponse.builder()
                    .replyId(index * 5 + i)
                    .content("content")
                    .member(MemberInfo.of(createMember((long) i)))
                    .createdDate(LocalDateTime.now())
                    .updatedDate(LocalDateTime.now())
                    .build();

            replies.add(reply);
        }
        return replies;
    }
}
