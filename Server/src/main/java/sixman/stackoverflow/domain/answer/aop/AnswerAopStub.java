package sixman.stackoverflow.domain.answer.aop;

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
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;
import sixman.stackoverflow.global.response.PageInfo;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
public class AnswerAopStub {

    @Around("execution(* sixman.stackoverflow.domain.answer.controller.AnswerController.getAnswer(..))")
    public Object getAnswer(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long answerId = (Long) args[0];

        AnswerResponse answer = AnswerResponse.builder()
                .answerId(answerId)
                .content("content")
                .member(MemberInfo.of(createMember(1L)))
                .recommend(10)
                .recommendType(TypeEnum.UPVOTE)
                .reply(createReplyResponse(1))
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(ApiSingleResponse.ok(answer));
    }

    @Around("execution(* sixman.stackoverflow.domain.answer.controller.AnswerController.getRepliesByAnswerId(..))")
    public Object getRepliesByAnswerId(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long answerId = (Long) args[0];
        int page = (int) args[1];

        List<ReplyResponse> replyResponses = getReplyResponses(1);
        Page<ReplyResponse> replyPage = new PageImpl<>(replyResponses, PageRequest.of(page - 1, 5), 20);

        return ResponseEntity.ok(ApiSingleResponse.ok(ApiPageResponse.ok(replyPage)));
    }

    @Around("execution(* sixman.stackoverflow.domain.answer.controller.AnswerController.updateAnswer(..))")
    public Object updateAnswer(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    @Around("execution(* sixman.stackoverflow.domain.answer.controller.AnswerController.upvoteAnswer(..))")
    public Object upvoteAnswer(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    @Around("execution(* sixman.stackoverflow.domain.answer.controller.AnswerController.downvoteAnswer(..))")
    public Object downvoteAnswer(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    @Around("execution(* sixman.stackoverflow.domain.answer.controller.AnswerController.deleteAnswer(..))")
    public Object deleteAnswer(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    @Around("execution(* sixman.stackoverflow.domain.answer.controller.AnswerController.createReply(..))")
    public Object createReply(ProceedingJoinPoint joinPoint) {

        URI uri = URI.create("/replies/1");

        return ResponseEntity.created(uri).build();
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

    protected AnswerResponse.AnswerReply createReplyResponse(int index) {

        List<ReplyResponse> replies = getReplyResponses(index);

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
