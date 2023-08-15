package sixman.stackoverflow.domain.question.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerCreateApiRequest;
import sixman.stackoverflow.domain.answer.service.AnswerService;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.controller.dto.QuestionCreateApiRequest;
import sixman.stackoverflow.domain.question.controller.dto.QuestionUpdateApiRequest;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.service.QuestionService;
import sixman.stackoverflow.domain.question.service.response.QuestionDetailResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionResponse;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.questionexception.InvalidPageParameterException;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;

import javax.validation.Valid;
import java.net.URI;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final MemberRepository memberRepository;
    private final AnswerService answerService;

    public QuestionController(QuestionService questionService, MemberRepository memberRepository, AnswerService answerService) {
        this.questionService = questionService;
        this.memberRepository = memberRepository;
        this.answerService = answerService;
    }



    //최초 질문 목록 조회 기능 구현(최신순 정렬 페이지 당 10개 글)
    @GetMapping
    public ResponseEntity<ApiPageResponse<QuestionResponse>> getQuestions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 0 || size <= 0) {
            throw new InvalidPageParameterException();
        }

        int adjustedPage = page - 1;

        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("createdDate").descending());
        Page<QuestionResponse> questions = questionService.getLatestQuestions(pageable);


        return ResponseEntity.ok(ApiPageResponse.ok(questions, "질문 목록 조회 성공"));
    }

    // 질문글 조회 기능 구현
    @GetMapping("/{question-id}")
    public ResponseEntity<ApiSingleResponse<QuestionDetailResponse>> getQuestionById(@PathVariable("question-id") @Positive Long questionId) {

        QuestionDetailResponse questionDetailResponse = questionService.getQuestionById(questionId);

        return ResponseEntity.ok(ApiSingleResponse.ok(questionDetailResponse, "질문 조회 성공"));
    }

    //필요없음
//    // 질문글 답변 페이징 조회
////    @GetMapping("/{questionId}/answers")
////    public ResponseEntity<ApiPageResponse<AnswerResponse>> getAnswersForQuestion(
////            @PathVariable @Positive Long questionId,
////            @RequestParam(defaultValue = "1") int page,
////            @RequestParam(defaultValue = "5") int size) {
////
////        Question question = questionService.getQuestionById(questionId);
////        Pageable pageable = PageRequest.of(page - 1, size);
////        Page<AnswerResponse> answerResponses = questionService.getAnswerResponsesForQuestion(question, pageable);
////
////        return ResponseEntity.ok(ApiPageResponse.ok(answerResponses, "답변 조회 성공"));
//    }


    // 질문글 생성 기능
    @PostMapping
    public ResponseEntity<ApiSingleResponse<QuestionDetailResponse>> createQuestion(
            @RequestBody @Valid QuestionCreateApiRequest questionCreateApiRequest) {

        Long memberId = SecurityUtil.getCurrentId();

        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalMember.isEmpty()) {
            throw new MemberNotFoundException();
        }

        Member member = optionalMember.get();
        Question question = questionCreateApiRequest.toEntity(member);
        Long questionId = questionService.createQuestion(question);

        URI uri = URI.create("/questions/" + questionId);

        return ResponseEntity.created(uri).build();
    }

    // 질문글 추천 기능
    @PatchMapping("/{question-id}/upvote")
    public ResponseEntity<ApiSingleResponse<Void>> recommendQuestion(@PathVariable("question-id") @Positive Long questionId) {
        questionService.addQuestionRecommend(questionId, TypeEnum.UPVOTE);
        return ResponseEntity.noContent().build();
    }

    // 질문글 비추천 기능
    @PatchMapping("/{question-id}/downvote")
    public ResponseEntity<ApiSingleResponse<Void>> disrecommendQuestion(@PathVariable("question-id") @Positive Long questionId) {
        questionService.addQuestionRecommend(questionId, TypeEnum.DOWNVOTE);
        return ResponseEntity.noContent().build();
    }


    //질문글 수정
    @PatchMapping("/{question-id}")
    public ResponseEntity<Void> updateQuestion(
            @PathVariable("question-id") @Positive Long questionId,
            @RequestBody @Valid QuestionUpdateApiRequest request) {

        questionService.updateQuestion(questionId, request.getTitle(), request.getContent());

        return ResponseEntity.noContent().build();
    }

    // 태그 수정 기능 -> 얘기해봐야 함
    @PatchMapping("/{question-Id}/tags")
    public ResponseEntity<ApiSingleResponse<Void>> updateTags(
            @PathVariable("question-id") @Positive Long questionId,
            @RequestBody List<String> tagNames) {

        questionService.updateTags(questionId, tagNames);
        ApiSingleResponse<Void> response = new ApiSingleResponse<>(null, 200, "Success", "OK");
        return ResponseEntity.ok(response);
    }
    // 질문글 삭제 기능
    @DeleteMapping("/{question-id}")
    public ResponseEntity<ApiSingleResponse<Void>> deleteQuestion(@PathVariable("question-id") @Positive Long questionId) {

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

        URI uri = URI.create("/answers/" + answerId);

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{question-id}/answers")
    public ResponseEntity<ApiPageResponse<AnswerResponse>> getAnswers(@PathVariable("question-id")Long questionId,
                                           @RequestParam(defaultValue = "1") int page
                                           ) {

        Page<AnswerResponse> answers = answerService.findAnswers(questionId, PageRequest.of(page - 1, 5));

        return ResponseEntity.ok(ApiPageResponse.ok(answers));
    }
}
