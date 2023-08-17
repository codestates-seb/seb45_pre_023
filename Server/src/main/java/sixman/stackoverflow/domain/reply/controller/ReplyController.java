package sixman.stackoverflow.domain.reply.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.reply.controller.dto.ReplyCreateApiRequest;
import sixman.stackoverflow.domain.reply.controller.dto.ReplyUpdateApiRequest;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.service.ReplyService;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;


import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }


//    @PostMapping("/questions/{question-id}/answers/{answer-id}/replies")
//    public ResponseEntity<Void> createReply(@PathVariable("answer-id")Long answerId,
//                                                       @RequestBody @Valid ReplyCreateApiRequest request) {
//
//        Long replyId = replyService.createReply(request.toServiceRequest(),answerId);
//
//        URI uri = URI.create("/replies/" + replyId);
//        return ResponseEntity.created(uri).build();
//    }

    @GetMapping("/{reply-id}") // 리플 단건 조회
    public ResponseEntity<ApiSingleResponse<ReplyResponse>> getReply(@PathVariable("reply-id") Long replyId) {

        ReplyResponse replyResponse = replyService.findReply(replyId);

        return ResponseEntity.ok(ApiSingleResponse.ok(replyResponse));

    }

    @PatchMapping("/{reply-id}")
    public ResponseEntity<Void> updateReply(@PathVariable("reply-id") Long replyId,
                                            @RequestBody @Valid ReplyUpdateApiRequest request) {


        replyService.updateReply(replyId, request.getContent());
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{reply-id}")
    public ResponseEntity<Void> deleteReply(@PathVariable("reply-id") Long replyId) {

        Long memberId = SecurityUtil.getCurrentId();

        replyService.deleteReply(replyId);

        return ResponseEntity.noContent().build();
    }


//    private ReplyResponse getReplyResponse(Long replyId) {
//        Reply reply = replyService.findReply(replyId);
//
//        ReplyResponse replyResponse = ReplyResponse.builder() // 초기화 과정
//                .replyId(reply.getReplyId())
//                .content(reply.getContent())
//                .member(reply.getMember().getMyInfo())
//                .build();
//
//        return replyResponse;


}



