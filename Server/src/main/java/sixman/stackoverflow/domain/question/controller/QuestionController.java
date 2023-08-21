package sixman.stackoverflow.domain.question.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.domain.answer.controller.dto.AnswerCreateApiRequest;
import sixman.stackoverflow.domain.answer.service.AnswerService;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.controller.dto.AnswerSortRequest;
import sixman.stackoverflow.domain.question.controller.dto.QuestionCreateApiRequest;
import sixman.stackoverflow.domain.question.controller.dto.QuestionSortRequest;
import sixman.stackoverflow.domain.question.controller.dto.QuestionUpdateApiRequest;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.service.QuestionService;
import sixman.stackoverflow.domain.question.service.response.QuestionDetailResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionResponse;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberBadCredentialsException;
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
@Validated
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
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(defaultValue = "CREATED_DATE") QuestionSortRequest sort, //createdDate, recommend, views
            @RequestParam(required = false) String tag) {

        int adjustedPage = page - 1;

        Pageable pageable = PageRequest.of(adjustedPage, size, Sort.by(sort.getValue()).descending());
        Page<QuestionResponse> questions = questionService.getLatestQuestions(pageable, tag);


        return ResponseEntity.ok(ApiPageResponse.ok(questions, "질문 목록 조회 성공"));
    }

    // 질문글 조회 기능 구현
        @GetMapping("/{question-id}")
        public ResponseEntity<ApiSingleResponse<QuestionDetailResponse>> getQuestionById(@PathVariable("question-id") @Positive Long questionId) {

            QuestionDetailResponse questionDetailResponse = questionService.getQuestionById(questionId);

            return ResponseEntity.ok(ApiSingleResponse.ok(questionDetailResponse, "질문 조회 성공"));
    }


    // 질문글 생성 기능
    @PostMapping
    public ResponseEntity<ApiSingleResponse<QuestionDetailResponse>> createQuestion(
            @RequestBody @Valid QuestionCreateApiRequest questionCreateApiRequest) {

        if (SecurityUtil.isLogin()) {

            Optional<Long> loggedInUserIdOpt = Optional.ofNullable(SecurityUtil.getCurrentId());
            if (!loggedInUserIdOpt.isPresent()) {
                throw new MemberNotFoundException();
            }
            Long currentId = loggedInUserIdOpt.get();

            Optional<Member> optionalMember = memberRepository.findById(currentId);

            if(!optionalMember.isPresent()){
                throw new MemberNotFoundException();
            }

            Member member = optionalMember.get();
            Question question = questionCreateApiRequest.toEntity(member);
            Long questionId = questionService.createQuestion(question, questionCreateApiRequest.getTagNames());

            URI uri = URI.create("/questions/" + questionId);

            return ResponseEntity.created(uri).build();
        }else{
            throw new MemberBadCredentialsException();
        }
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

        questionService.updateQuestion(
                questionId,
                request.getTitle(),
                request.getDetail(),
                request.getExpect(),
                request.getTagNames()
        );

        return ResponseEntity.noContent().build();
    }

    // 질문글 삭제 기능
    @DeleteMapping("/{question-id}")
    public ResponseEntity<ApiSingleResponse<Void>> deleteQuestion(@PathVariable("question-id") @Positive Long questionId) {

        questionService.deleteQuestion(questionId);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{question-id}/answers")
    public ResponseEntity<Void> createAnswer(@PathVariable("question-id")Long questionId,
                                             @RequestBody @Valid AnswerCreateApiRequest request) {
        Long answerId = answerService.createAnswer(request.toServiceRequest(), questionId);

        URI uri = URI.create("/answers/" + answerId);

        return ResponseEntity.created(uri).build();
    }

    // 답변 페이징 기능
    @GetMapping("/{question-id}/answers")
    public ResponseEntity<ApiPageResponse<AnswerResponse>> getAnswers(
                            @PathVariable("question-id")Long questionId,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "CREATED_DATE") AnswerSortRequest sort) { //createdDate, recommend

        Pageable pageable = PageRequest.of(page - 1, 5, Sort.by(sort.getValue()).descending());

        Page<AnswerResponse> answers = answerService.findAnswers(questionId, pageable);

        return ResponseEntity.ok(ApiPageResponse.ok(answers));
    }
}
