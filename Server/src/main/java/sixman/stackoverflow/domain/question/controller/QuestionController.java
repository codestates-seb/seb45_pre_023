package sixman.stackoverflow.domain.question.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerCreateApiRequest;
import sixman.stackoverflow.domain.answer.service.AnswerService;
import sixman.stackoverflow.domain.question.controller.dto.QuestionCreateApiRequest;
import sixman.stackoverflow.domain.question.controller.dto.QuestionUpdateApiRequest;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.service.QuestionService;
import sixman.stackoverflow.domain.question.service.response.QuestionResponse;
import sixman.stackoverflow.domain.question.controller.dto.QuestionTagCreateApiRequest;
import sixman.stackoverflow.domain.question.service.response.ApiListResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionTagResponse;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.exception.businessexception.questionexception.InvalidPageParameterException;
import sixman.stackoverflow.global.exception.businessexception.questionexception.QuestionNotFoundException;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;
import sixman.stackoverflow.global.response.PageInfo;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    private final AnswerService answerService;

    public QuestionController(QuestionService questionService, AnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }



    //최초 질문 목록 조회 기능 구현(최신순 정렬 페이지 당 10개 글)
    @GetMapping
    public ResponseEntity<ApiPageResponse<QuestionResponse>> getQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0 || size <= 0) {
            throw new InvalidPageParameterException();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<QuestionResponse> questions = questionService.getLatestQuestions(pageable)
                .map(QuestionResponse::from);

        PageInfo pageInfo = PageInfo.of(questions);

        return ResponseEntity.ok(ApiPageResponse.ok(questions, "질문 목록 조회 성공"));
    }

    // 질문글 조회 기능 구현
    @GetMapping("/{memberId}/questions")
    public ResponseEntity<ApiSingleResponse<QuestionResponse>> getQuestionById(@PathVariable Long questionId) {
        Question question = questionService.getQuestionById(questionId);

        if (question == null) {
            throw new QuestionNotFoundException();
        }

        QuestionResponse questionResponse = QuestionResponse.from(question);

        return ResponseEntity.ok(ApiSingleResponse.ok(questionResponse, "질문 조회 성공"));
    }

    // 질문의 태그 리스트 조회
    @GetMapping("/{questionId}/tags")
    public ResponseEntity<ApiListResponse<QuestionTagResponse>> getQuestionTags(
            @PathVariable Long questionId) {
        List<QuestionTagResponse> tagResponses = questionService.getQuestionTags(questionId);
        return ResponseEntity.ok(new ApiListResponse<>(tagResponses));
    }

    // 질문글 생성 기능

    @PostMapping("/members/{memberId}/questions")
    public ResponseEntity<ApiSingleResponse<QuestionResponse>> createQuestion(
            @PathVariable Long memberId,
            @RequestBody @Valid QuestionCreateApiRequest questionCreateApiRequest) {
        Question question = questionCreateApiRequest.toEntity();
        Question savedQuestion = questionService.createQuestion(question);
        QuestionResponse questionResponse = QuestionResponse.from(savedQuestion);

        return ResponseEntity.noContent().build();
    }

    // 질문글 추천 기능
    @PostMapping("/{questionId}/upvote")
    public ResponseEntity<ApiSingleResponse<Void>> recommendQuestion(@PathVariable Long questionId) {
        questionService.addQuestionRecommend(questionId, TypeEnum.UPVOTE);
        return ResponseEntity.noContent().build();
    }

    // 질문글 비추천 기능
    @PostMapping("/{questionId}/downvote")
    public ResponseEntity<ApiSingleResponse<Void>> disrecommendQuestion(@PathVariable Long questionId) {
        questionService.addQuestionRecommend(questionId, TypeEnum.DOWNVOTE);
        return ResponseEntity.noContent().build();
    }

    // 태그 추가 기능
    @PostMapping("/{questionId}/tags")
    public ResponseEntity<ApiSingleResponse<Void>> addTagsToQuestion(
            @PathVariable Long questionId,
            @RequestBody List<QuestionTagCreateApiRequest> tagCreateRequests) {
        questionService.addTagsToQuestion(questionId, tagCreateRequests);
        return ResponseEntity.noContent().build();
    }

    //질문글 수정
    @PatchMapping("/{questionId}")
    public ResponseEntity<ApiSingleResponse<QuestionResponse>> updateQuestion(
            @PathVariable Long questionId,
            @RequestBody @Valid QuestionUpdateApiRequest questionUpdateApiRequest) {
        Question question = questionService.getQuestionById(questionId);
        Question updatedQuestion = questionUpdateApiRequest.updateEntity(question);
        Question updated = questionService.updateQuestion(questionId, updatedQuestion);
        QuestionResponse questionResponse = QuestionResponse.from(updated);

        return ResponseEntity.noContent().build();
    }

    // 태그 수정 기능
    @PatchMapping("/{questionId}/tags")
    public ResponseEntity<ApiSingleResponse<Void>> updateTags(
            @PathVariable Long questionId,
            @RequestBody List<String> tagNames) {
        questionService.updateTags(questionId, tagNames);
        ApiSingleResponse<Void> response = new ApiSingleResponse<>(null, 200, "Success", "OK");
        return ResponseEntity.ok(response);
    }
    // 질문글 삭제 기능
    @DeleteMapping("/{questionId}")
    public ResponseEntity<ApiSingleResponse<Void>> deleteQuestion(@PathVariable Long questionId) {
        Question existingQuestion = questionService.getQuestionById(questionId);

        // 질문이 존재하는지 확인
        if (existingQuestion == null) {
            throw new QuestionNotFoundException();
        }

        questionService.deleteQuestion(questionId);

        return ResponseEntity.noContent().build();
    }

    // 태그 삭제 기능
    @DeleteMapping("/{questionId}/tags")
    public ResponseEntity<ApiSingleResponse<Void>> removeTagsFromQuestion(
            @PathVariable Long questionId,
            @RequestBody List<String> tagNames) {
        questionService.removeTagsFromQuestion(questionId, tagNames);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{question-id}/answers")
    public ResponseEntity<Void> createAnswer(@PathVariable("question-id")Long questionId,
                                             @RequestBody @Valid AnswerCreateApiRequest request) {
        Long answerId = answerService.createAnswer(request.toServiceRequest(), questionId);

        URI uri = URI.create("/{question-id}/answers/" + answerId);

        return ResponseEntity.created(uri).build();
    }
}
