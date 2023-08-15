package sixman.stackoverflow.domain.member.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;
import sixman.stackoverflow.global.response.PageInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
public class MemberAop {

    @Around("execution(* sixman.stackoverflow.domain.member.controller.MemberController.getMember(..))")
    public Object getMember(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long memberId = (Long) args[0];

        return ResponseEntity.ok(ApiSingleResponse.ok(getMemberResponse(memberId)));
    }

    @Around("execution(* sixman.stackoverflow.domain.member.controller.MemberController.getMemberQuestion(..))")
    public Object getMemberQuestion(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long memberId = (Long) args[0];
        int page = (int) args[1];

        MemberResponse.MemberQuestionPageResponse memberQuestionResponse = getMemberResponse(memberId).getQuestion();

        Page<MemberResponse.MemberQuestion> memberQuestionPage
                = new PageImpl<>(
                memberQuestionResponse.getQuestions(),
                PageRequest.of(page - 1, 5),
                memberQuestionResponse.getPageInfo().getTotalSize());

        return ResponseEntity.ok(ApiPageResponse.ok(memberQuestionPage));
    }

    @Around("execution(* sixman.stackoverflow.domain.member.controller.MemberController.getMemberAnswer(..))")
    public Object getMemberAnswer(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long memberId = (Long) args[0];
        int page = (int) args[1];

        MemberResponse.MemberAnswerPageResponse memberAnswerPageResponse = getMemberResponse(memberId).getAnswer();

        Page<MemberResponse.MemberAnswer> memberAnswerPage
                = new PageImpl<>(
                memberAnswerPageResponse.getAnswers(),
                PageRequest.of(page - 1, 5),
                memberAnswerPageResponse.getPageInfo().getTotalSize());

        return ResponseEntity.ok(ApiPageResponse.ok(memberAnswerPage));
    }

    @Around("execution(* sixman.stackoverflow.domain.member.controller.MemberController.updateMember(..))")
    public Object updateMember(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    @Around("execution(* sixman.stackoverflow.domain.member.controller.MemberController.updatePassword(..))")
    public Object updatePassword(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    @Around("execution(* sixman.stackoverflow.domain.member.controller.MemberController.updateImage(..))")
    public Object updateImage(ProceedingJoinPoint joinPoint) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "https://sixman-images-test.s3.ap-northeast-2.amazonaws.com/test.png");

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @Around("execution(* sixman.stackoverflow.domain.member.controller.MemberController.updateImage(..))")
    public Object deleteImage(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    @Around("execution(* sixman.stackoverflow.domain.member.controller.MemberController.deleteMember(..))")
    public Object deleteMember(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    private MemberResponse getMemberResponse(Long memberId) {
        List<MemberResponse.MemberQuestion> questions = new ArrayList<>();
        List<MemberResponse.MemberAnswer> answers = new ArrayList<>();

        for(long i = 1; i <= 5; i++){

            MemberResponse.MemberQuestion question = MemberResponse.MemberQuestion.builder()
                    .questionId(i)
                    .title("title " + i)
                    .views(100)
                    .recommend(10)
                    .createdDate(LocalDateTime.now().minusDays(1))
                    .updatedDate(LocalDateTime.now())
                    .build();

            questions.add(question);

            MemberResponse.MemberAnswer answer = MemberResponse.MemberAnswer.builder()
                    .answerId(i)
                    .questionTitle("title " + i)
                    .questionId(i)
                    .content("content " + i)
                    .recommend(10)
                    .createdDate(LocalDateTime.now().minusDays(1))
                    .updatedDate(LocalDateTime.now())
                    .build();

            answers.add(answer);
        }

        MemberResponse.MemberQuestionPageResponse question = MemberResponse.MemberQuestionPageResponse.builder()
                .questions(questions)
                .pageInfo(PageInfo.of(new PageImpl(questions, PageRequest.of(0, 5), 100)))
                .build();

        MemberResponse.MemberAnswerPageResponse answer = MemberResponse.MemberAnswerPageResponse.builder()
                .answers(answers)
                .pageInfo(PageInfo.of(new PageImpl(answers, PageRequest.of(0, 5), 100)))
                .build();

        List<MemberResponse.MemberTag> tags = new ArrayList<>();

        for(long i = 1; i <= 5; i++){
            MemberResponse.MemberTag tag = MemberResponse.MemberTag.builder()
                    .tagId(i)
                    .tagName("tag " + i)
                    .build();

            tags.add(tag);
        }

        MemberResponse memberResponse = MemberResponse.builder()
                .memberId(memberId)
                .email("test@test.com")
                .nickname("nickname")
                .image("https://sixman-images-test.s3.ap-northeast-2.amazonaws.com/test.png")
                .myIntro("hi! im test")
                .authority(Authority.ROLE_USER)
                .question(question)
                .answer(answer)
                .tags(tags)
                .build();

        return memberResponse;
    }
}
