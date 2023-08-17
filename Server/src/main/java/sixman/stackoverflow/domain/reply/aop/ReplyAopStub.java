package sixman.stackoverflow.domain.reply.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;

import java.time.LocalDateTime;

//@Component
@Aspect
public class ReplyAopStub {

    @Around("execution(* sixman.stackoverflow.domain.reply.controller.ReplyController.getReply(..))")
    public Object getReply(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        Long replyId = (Long) args[0];

        ReplyResponse replyResponse = ReplyResponse.builder()
                .replyId(replyId)
                .content("reply content")
                .member(MemberInfo.of(createMember(1L)))
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(ApiSingleResponse.ok(replyResponse));
    }

    @Around("execution(* sixman.stackoverflow.domain.reply.controller.ReplyController.updateReply(..))")
    public Object updateReply(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
    }

    @Around("execution(* sixman.stackoverflow.domain.reply.controller.ReplyController.deleteReply(..))")
    public Object deleteReply(ProceedingJoinPoint joinPoint) {

        return ResponseEntity.noContent().build();
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
}
