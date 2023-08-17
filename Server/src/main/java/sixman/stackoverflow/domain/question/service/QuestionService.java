package sixman.stackoverflow.domain.question.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.service.AnswerService;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.question.controller.dto.QuestionTagCreateApiRequest;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.domain.question.service.response.QuestionDetailResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionTagResponse;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.domain.questionrecommend.repository.QuestionRecommendRepository;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.domain.tag.repository.TagRepository;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.questionexception.QuestionAlreadyVotedException;
import sixman.stackoverflow.global.exception.businessexception.questionexception.QuestionNotFoundException;
import sixman.stackoverflow.global.response.PageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionRecommendRepository questionRecommendRepository;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final AnswerService answerService;

    public QuestionService(QuestionRepository questionRepository,
                           QuestionRecommendRepository questionRecommendRepository,
                           MemberRepository memberRepository,
                           TagRepository tagRepository,
                           AnswerService answerService) {
        this.questionRepository = questionRepository;
        this.questionRecommendRepository = questionRecommendRepository;
        this.memberRepository = memberRepository;
        this.tagRepository = tagRepository;
        this.answerService = answerService;
    }

    public Page<QuestionResponse> getLatestQuestions(Pageable pageable) {
        Page<Question> questions = questionRepository.findAllByOrderByCreatedDateDesc(pageable);
        return questions.map(QuestionResponse::of);
    }

    public QuestionDetailResponse getQuestionById(Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findByQuestionId(questionId);

        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();

            // 조회수 증가 로직 추가
            question.setViews(question.getViews() + 1);
            questionRepository.save(question);

            Page<AnswerResponse> pagedAnswers = answerService.findAnswers(questionId, PageRequest.of(0, 5));
            List<AnswerResponse> answerResponses = pagedAnswers.getContent();

            QuestionDetailResponse.QuestionAnswer questionAnswer = QuestionDetailResponse.QuestionAnswer.builder()
                    .answers(answerResponses)
                    .pageInfo(PageInfo.of(pagedAnswers))
                    .build();

            return QuestionDetailResponse.of(question, questionAnswer);
        }else {
            throw new QuestionNotFoundException();
        }
    }


    public List<QuestionTagResponse> getQuestionTags(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow();
        List<Tag> tags = question.getQuestionTags().stream().map(QuestionTag::getTag).collect(Collectors.toList());

        return QuestionTagResponse.of(tags);
    }

    public Long createQuestion(Question question) {
        return questionRepository.save(question).getQuestionId();
    }

    public void addTagsToQuestion(Long questionId, List<QuestionTagCreateApiRequest> tagCreateRequests) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow();

        for (QuestionTagCreateApiRequest tagCreateRequest : tagCreateRequests) {
            String tagName = tagCreateRequest.getTagName();
            Tag tag = createOrGetTag(tagName);
            QuestionTag questionTag = QuestionTag.createQuestionTag(question, tag);
            question.getQuestionTags().add(questionTag);
        }

        questionRepository.save(question);
    }

    public Question updateQuestion(Long questionId, String title, String detail, String expect) {
        Question existingQuestion = questionRepository.findById(questionId)
                .orElseThrow();

        Long loggedInUserId = SecurityUtil.getCurrentId();
        Long questionAuthorId = questionRepository.findMemberIdByQuestionId(questionId);

        if (!loggedInUserId.equals(questionAuthorId)) {
            throw new MemberAccessDeniedException();
        }

        existingQuestion.setTitle(title);
        existingQuestion.setDetail(detail);
        existingQuestion.setExpect(expect);

        return questionRepository.save(existingQuestion);
    }

    public void updateTags(Long questionId, List<String> tagNames) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow();

        Long loggedInUserId = SecurityUtil.getCurrentId();
        Long questionAuthorId = questionRepository.findMemberIdByQuestionId(questionId);

        if (!loggedInUserId.equals(questionAuthorId)) {
            throw new MemberAccessDeniedException();
        }

        List<QuestionTag> newQuestionTags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = createOrGetTag(tagName);
            QuestionTag questionTag = QuestionTag.createQuestionTag(question, tag);
            newQuestionTags.add(questionTag);
        }

        question.setQuestionTags(newQuestionTags);
        questionRepository.save(question);
    }

    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow();

        Long loggedInUserId = SecurityUtil.getCurrentId();
        Long questionAuthorId = questionRepository.findMemberIdByQuestionId(questionId);

        if (!loggedInUserId.equals(questionAuthorId)) {
            throw new MemberAccessDeniedException();
        }
        questionRepository.delete(question);
    }

    public void removeTagsFromQuestion(Long questionId, List<String> tagNames) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow();

        List<QuestionTag> updatedTags = question.getQuestionTags().stream()
                .filter(questionTag -> !tagNames.contains(questionTag.getTag().getTagName()))
                .collect(Collectors.toList());

        question.setQuestionTags(updatedTags);
        questionRepository.save(question);
    }

    public void addQuestionRecommend(Long questionId, TypeEnum type) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(QuestionNotFoundException::new);

        Long currentId = SecurityUtil.getCurrentId();

        Member currentMember = memberRepository.findById(currentId)
                .orElseThrow(MemberNotFoundException::new);

        if (question.hasRecommendationFrom(currentMember)) {
            Optional<QuestionRecommend> existingRecommend = questionRecommendRepository.findByMemberAndQuestionAndType(currentMember, question, type);
            if (existingRecommend.isPresent()) {
                // 이미 추천한 경우
                if (existingRecommend.get().getType() == type) {
                    // 이미 같은 타입의 추천이면 추천 취소
                    questionRecommendRepository.delete(existingRecommend.get());
                    existingRecommend.get().applyRecommend(); // 추천 취소 적용
                } else {
                    throw new QuestionAlreadyVotedException();
                }
            } else {
                throw new QuestionAlreadyVotedException();
            }
        } else {
            // 추천 또는 비추천 추가
            QuestionRecommend newRecommend = QuestionRecommend.builder()
                    .type(type)
                    .member(currentMember)
                    .question(question)
                    .build();
            newRecommend.applyRecommend(); // 추천 또는 비추천 적용
            questionRecommendRepository.save(newRecommend);
        }

        questionRepository.save(question);
    }

    private Tag createOrGetTag(String tagName) {
        Optional<Tag> tagOptional = tagRepository.findByTagName(tagName);
        return tagOptional.orElseGet(() -> createTag(tagName));
    }

    private Tag createTag(String tagName) {
        Tag tag = Tag.builder()
                .tagName(tagName)
                .build();
        return tagRepository.save(tag);
    }


}
