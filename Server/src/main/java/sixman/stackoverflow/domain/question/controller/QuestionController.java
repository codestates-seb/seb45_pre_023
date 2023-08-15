package sixman.stackoverflow.domain.question.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
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
    @PostMapping
    public ResponseEntity<ApiSingleResponse<QuestionResponse>> createQuestion(
            @RequestBody @Valid QuestionCreateApiRequest questionCreateApiRequest) {

        Long memberId = SecurityUtil.getCurrentId();

        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException();
        }

        Member member = optionalMember.get();
        Question question = questionCreateApiRequest.toEntity(member);
        Question savedQuestion = questionService.createQuestion(question);
        QuestionResponse questionResponse = QuestionResponse.from(savedQuestion);

        return ResponseEntity.noContent().build();
    }

    // 질문글 추천 기능
    @PatchMapping("/{question-id}/upvote")
    public ResponseEntity<ApiSingleResponse<Void>> recommendQuestion(@PathVariable @Positive Long questionId) {
        questionService.addQuestionRecommend(questionId, TypeEnum.UPVOTE);
        return ResponseEntity.noContent().build();
    }

    // 질문글 비추천 기능
    @PatchMapping("/{question-id}/downvote")
    public ResponseEntity<ApiSingleResponse<Void>> disrecommendQuestion(@PathVariable @Positive Long questionId) {
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
    @PatchMapping("/{question-id}")
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
    @PatchMapping("/{question-Id}/tags")
    public ResponseEntity<ApiSingleResponse<Void>> updateTags(
            @PathVariable Long questionId,
            @RequestBody List<String> tagNames) {
        questionService.updateTags(questionId, tagNames);
        ApiSingleResponse<Void> response = new ApiSingleResponse<>(null, 200, "Success", "OK");
        return ResponseEntity.ok(response);
    }
    // 질문글 삭제 기능
    @DeleteMapping("/{question-id}")
    public ResponseEntity<ApiSingleResponse<Void>> deleteQuestion(@PathVariable @Positive Long questionId,
                                                                  Authentication authentication) {
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
}
