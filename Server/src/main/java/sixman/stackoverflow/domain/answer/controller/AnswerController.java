package sixman.stackoverflow.domain.answer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerCreateApiRequest;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerUpdateApiRequest;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.service.AnswerService;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;


@RestController
@RequestMapping("/")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }
    @PostMapping("/questions/{question-id}/answers")
    public ResponseEntity<Void> postAnswer(@PathVariable("question-id")Long questionId,
                                           @RequestBody AnswerCreateApiRequest request) {
        answerService.createAnswer(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/questions/{question-id}/answers/{answer-id}")
    public ResponseEntity<ApiSingleResponse<AnswerResponse>> getAnswer(@PathVariable("answer-id") Long answerId
                                                                       ) {
        AnswerResponse answerResponse = getAnswerResponse(answerId);

        return ResponseEntity.ok(ApiSingleResponse.ok(answerResponse));

    }
    @GetMapping("/questions/{question-id}/answers/{answer-id}")



    @PatchMapping("/questions/{question-id}/answers/{answer-id}")
    public ResponseEntity<Void> patchAnswer(@PathVariable("answer-id") Long answerId,
                                           @RequestBody AnswerUpdateApiRequest request) {
        answerService.updateAnswer(answerId, request.getContent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/questions/{question-id}/answers/{answer-id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable("answer-id") Long answerId) {

        answerService.deleteAnswer(answerId);

        return ResponseEntity.noContent().build();
    }

    private AnswerResponse getAnswerResponse(Long answerId) {
        Answer answer = answerService.findAnswer(answerId);

        AnswerResponse answerResponse = AnswerResponse.builder() // 초기화 과정
                .answerId(answer.getAnswerId())
                .content(answer.getContent())
                .views(answer.getViews())
                //.recommends(answer.getAnswerRecommends()) 서비스 로직에 ?
                .build();
        return answerResponse;
    }
}


