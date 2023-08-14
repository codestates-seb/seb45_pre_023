package sixman.stackoverflow.domain.answer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerCreateApiRequest;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerUpdateApiRequest;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.service.AnswerService;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.answerrecommend.service.AnswerRecommendService;
import sixman.stackoverflow.domain.reply.entity.Reply;
import sixman.stackoverflow.domain.reply.service.ReplyService;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.response.ApiSingleResponse;


import java.util.List;

@RestController
@RequestMapping("/")
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
    @PostMapping("/questions/{question-id}/answers")
    public ResponseEntity<Void> postAnswer(@PathVariable("question-id")Long questionId,
                                           @RequestBody AnswerCreateApiRequest request) {
        answerService.createAnswer(request, questionId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/questions/{question-id}/answers/{answer-id}")
    public ResponseEntity<ApiSingleResponse<AnswerResponse>> getAnswer(@PathVariable("answer-id") Long answerId
                                                                       ) {
        AnswerResponse answerResponse = getAnswerResponse(answerId);
//        int upvoteCount = answerResponse.getAnswer().getRecommendCount();
//        int downvoteCount = answerResponse.getAnswer().getDownvoteCount();
//        answerResponse.setUpvoteCount(upvoteCount);
//        answerResponse.setDownvoteCount(downvoteCount); // 구현중

        return ResponseEntity.ok(ApiSingleResponse.ok(answerResponse));

    }
    @GetMapping("/questions/{question-id}/answers/{answer-id}")



    @PatchMapping("/questions/{question-id}/answers/{answer-id}")
    public ResponseEntity<Void> updateAnswer(@PathVariable("answer-id") Long answerId,
                                           @RequestBody AnswerUpdateApiRequest request) {

        Long memberId = SecurityUtil.getCurrentId();

        answerService.updateAnswer(answerId, request.getContent(), memberId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{answer-id}/upvote")
    public ResponseEntity<Void> upvoteAnswer(@PathVariable("answer-id") Long answerId) {
        Long memberId = SecurityUtil.getCurrentId();
        answerRecommendService.recommendAnswer(answerId, memberId, TypeEnum.UPVOTE);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{answer-id}/downvote")
    public ResponseEntity<Void> downvoteAnswer(@PathVariable Long answerId) {
        Long memberId = SecurityUtil.getCurrentId();
        answerRecommendService.recommendAnswer(answerId, memberId, TypeEnum.DOWNVOTE);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/questions/{question-id}/answers/{answer-id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable("answer-id") Long answerId) {

        Long memberId = SecurityUtil.getCurrentId();
        answerService.deleteAnswer(answerId, memberId);

        return ResponseEntity.noContent().build();
    }

    private AnswerResponse getAnswerResponse(Long answerId) {
        Answer answer = answerService.findAnswer(answerId);

       // List<Reply> replies = replyService. // 구현중

        AnswerResponse answerResponse = AnswerResponse.builder() // 초기화 과정
                .answerId(answer.getAnswerId())
                .content(answer.getContent())
                //.recommends(answer.getAnswerRecommends()) 서비스 로직에 ?
                .build();
        return answerResponse;
    }
}


