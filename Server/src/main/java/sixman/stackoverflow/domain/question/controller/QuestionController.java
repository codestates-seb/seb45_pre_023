package sixman.stackoverflow.domain.question.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.controller.dto.QuestionCreateApiRequest;
import sixman.stackoverflow.domain.question.controller.dto.QuestionUpdateApiRequest;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.service.QuestionService;
import sixman.stackoverflow.domain.question.service.response.QuestionResponse;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.questionexception.InvalidPageParameterException;
import sixman.stackoverflow.global.exception.businessexception.questionexception.QuestionNotFoundException;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;
import sixman.stackoverflow.global.response.PageInfo;
import org.springframework.security.core.Authentication;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final MemberRepository memberRepository;

    public QuestionController(QuestionService questionService,
                              MemberRepository memberRepository) {
        this.questionService = questionService;
        this.memberRepository = memberRepository;
    }

    //최초 질문 목록 조회 기능 구현(최신순 정렬 페이지 당 10개 글)
    @GetMapping
    public ResponseEntity<ApiPageResponse<QuestionResponse>> getQuestions(
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "10") @Positive int size) {


        int adjustedPage = page - 1;


        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by("createdDate").descending());
        Page<QuestionResponse> questions = questionService.getLatestQuestions(pageable)
                .map(QuestionResponse::fromQuestion);

        PageInfo pageInfo = PageInfo.of(questions);

        return ResponseEntity.ok(ApiPageResponse.ok(questions, "질문 목록 조회 성공"));
    }

    // 질문글 조회 기능 구현
    @GetMapping("/{question-Id}")
    public ResponseEntity<ApiSingleResponse<QuestionResponse>> getQuestionById(@PathVariable @Positive Long questionId) {
        Question question = questionService.getQuestionById(questionId);

        if (question == null) {
            throw new QuestionNotFoundException();
        }

        QuestionResponse questionResponse = QuestionResponse.from(question);

        return ResponseEntity.ok(ApiSingleResponse.ok(questionResponse, "질문 조회 성공"));
    }

    // 질문글 답변 페이징 조회
    @GetMapping("/{questionId}/answers")
    public ResponseEntity<ApiPageResponse<AnswerResponse>> getAnswersForQuestion(
            @PathVariable @Positive Long questionId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        Question question = questionService.getQuestionById(questionId);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<AnswerResponse> answerResponses = questionService.getAnswerResponsesForQuestion(question, pageable);

        return ResponseEntity.ok(ApiPageResponse.ok(answerResponses, "답변 조회 성공"));
    }


    // 질문글 생성 기능

    @PostMapping
    public ResponseEntity<ApiSingleResponse<QuestionResponse>> createQuestion(
            @PathVariable @Positive Long memberId,
            @RequestBody @Valid QuestionCreateApiRequest questionCreateApiRequest) {
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
    @PatchMapping("/{question-Id}/upvote")
    public ResponseEntity<ApiSingleResponse<Void>> recommendQuestion(@PathVariable @Positive Long questionId) {
        questionService.addQuestionRecommend(questionId, TypeEnum.UPVOTE);
        return ResponseEntity.noContent().build();
    }

    // 질문글 비추천 기능
    @PatchMapping("/{question-Id}/downvote")
    public ResponseEntity<ApiSingleResponse<Void>> disrecommendQuestion(@PathVariable @Positive Long questionId) {
        questionService.addQuestionRecommend(questionId, TypeEnum.DOWNVOTE);
        return ResponseEntity.noContent().build();
    }


    //질문글 수정
    @PatchMapping("/{question-Id}")
    public ResponseEntity<ApiSingleResponse<QuestionResponse>> updateQuestion(
            @PathVariable @Positive Long questionId,
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
            @PathVariable @Positive Long questionId,
            @RequestBody List<String> tagNames) {
        questionService.updateTags(questionId, tagNames);
        ApiSingleResponse<Void> response = new ApiSingleResponse<>(null, 200, "Success", "OK");
        return ResponseEntity.ok(response);
    }
    // 질문글 삭제 기능
    @DeleteMapping("/{question-Id}")
    public ResponseEntity<ApiSingleResponse<Void>> deleteQuestion(@PathVariable @Positive Long questionId) {
        Question existingQuestion = questionService.getQuestionById(questionId);

        // 질문이 존재하는지 확인
        if (existingQuestion == null) {
            throw new QuestionNotFoundException();
        }

        questionService.deleteQuestion(questionId);

        return ResponseEntity.noContent().build();
    }

}
