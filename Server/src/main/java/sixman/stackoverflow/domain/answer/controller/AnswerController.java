package sixman.stackoverflow.domain.answer.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerCreateApiRequest;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerUpdateApiRequest;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.service.AnswerService;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.answerrecommend.service.AnswerRecommendService;
import sixman.stackoverflow.domain.reply.controller.dto.ReplyCreateApiRequest;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.service.ReplyService;
import sixman.stackoverflow.domain.reply.service.dto.response.ReplyResponse;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;


import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;
    private final ReplyService replyService;
    private final AnswerRecommendService answerRecommendService;

    public AnswerController(AnswerService answerService,
                            ReplyService replyService,
                            AnswerRecommendService answerRecommendService) {
        this.answerService = answerService;
        this.replyService = replyService;
        this.answerRecommendService = answerRecommendService;
    }
//    @PostMapping("/")
//    public ResponseEntity<Void> createAnswer(@PathVariable("question-id")Long questionId,
//                                           @RequestBody @Valid AnswerCreateApiRequest request) {
//        Long answerId = answerService.createAnswer(request.toServiceRequest(), questionId);
//
//        URI uri = URI.create("/questions/{question-id}/answers/" + answerId);
//
//        return ResponseEntity.created(uri).build();
//    }

    @GetMapping("/{answer-id}")
    public ResponseEntity<ApiSingleResponse<AnswerResponse>> getAnswer(@PathVariable("answer-id") Long answerId) {

        AnswerResponse answerResponse = answerService.findAnswer(answerId);

        return ResponseEntity.ok(ApiSingleResponse.ok(answerResponse));
    }

    @GetMapping("/{answer-id}/replies")
    public ResponseEntity<ApiPageResponse<ReplyResponse>> getRepliesByAnswerId(@PathVariable("answer-id") Long answerId,
                                                                    @RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ReplyResponse> repliesPage = replyService.getRepliesPaged(answerId, pageable);

        return ResponseEntity.ok(ApiPageResponse.ok(repliesPage));
    }


    @PatchMapping("/{answer-id}")
    public ResponseEntity<Void> updateAnswer(@PathVariable("answer-id") Long answerId,
                                           @RequestBody @Valid AnswerUpdateApiRequest request) {

        Long memberId = SecurityUtil.getCurrentId();

        answerService.updateAnswer(answerId, request.getContent(), memberId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{answer-id}/upvote")
    public ResponseEntity<Void> upvoteAnswer(@PathVariable("answer-id") Long answerId) {
        Long memberId = SecurityUtil.getCurrentId();
        answerRecommendService.recommendAnswer(answerId, memberId, TypeEnum.UPVOTE);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{answer-id}/downvote")
    public ResponseEntity<Void> downvoteAnswer(@PathVariable("answer-id") Long answerId) {
        Long memberId = SecurityUtil.getCurrentId();
        answerRecommendService.recommendAnswer(answerId, memberId, TypeEnum.DOWNVOTE);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{answer-id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable("answer-id") Long answerId) {

        Long memberId = SecurityUtil.getCurrentId();
        answerService.deleteAnswer(answerId, memberId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("{answer-id}/replies") //answer에 대한 질문 생성
    public ResponseEntity<Void> createReply(@PathVariable("answer-id")Long answerId,
                                            @RequestBody @Valid ReplyCreateApiRequest request) {

        Long replyId = replyService.createReply(request.toServiceRequest(),answerId);

        URI uri = URI.create("/replies/" + replyId);
        return ResponseEntity.created(uri).build();
    }

    private AnswerResponse getAnswerResponse(Long answerId) {
//        Answer answer = answerService.findAnswer(answerId);
//
//        List<ReplyResponse> replyResponses = replyService.getReplies(answerId);
//
//
//        AnswerResponse answerResponse = AnswerResponse.builder() // 초기화 과정
//                .answerId(answer.getAnswerId())
//                .content(answer.getContent())
//                .upvoteCount(answer.getUpvoteCount())
//                .downvoteCount(answer.getDownvoteCount())
//                .replies(replyResponses)
//                .build();
//        return answerResponse;

        return null;
    }
}


