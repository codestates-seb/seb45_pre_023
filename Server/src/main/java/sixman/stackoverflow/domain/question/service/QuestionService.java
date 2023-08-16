package sixman.stackoverflow.domain.question.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
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
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.questionexception.QuestionAlreadyVotedException;

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
    private final AnswerRepository answerRepository;

    public QuestionService(QuestionRepository questionRepository,
                           QuestionRecommendRepository questionRecommendRepository,
                           MemberRepository memberRepository,
                           TagRepository tagRepository,
                           AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.questionRecommendRepository = questionRecommendRepository;
        this.memberRepository = memberRepository;
        this.tagRepository = tagRepository;
        this.answerRepository = answerRepository;
    }

    public Page<QuestionResponse> getLatestQuestions(Pageable pageable) {

//        return questionRepository.findAllByOrderByCreatedDateDesc(pageable);
        return null;
    }

    public QuestionDetailResponse getQuestionById(Long questionId) {
        /*return questionRepository.findById(questionId)
                .orElse(null);*/

        return null;
    }

    public Page<AnswerResponse> getAnswerResponsesForQuestion(Question question, Pageable pageable) {
//        Page<Answer> answers = answerRepository.findByQuestion(question, pageable);
//        List<AnswerResponse> answerResponses = answers.getContent().stream()
//                .map(answer -> AnswerResponse.answerfrom(answer))// answerfrom() 메서드 구현
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(answerResponses, pageable, answers.getTotalElements());

        return null;
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

        existingQuestion.setTitle(title);
        existingQuestion.setDetail(detail);
        existingQuestion.setExpect(expect);

        return questionRepository.save(existingQuestion);
    }

    public void updateTags(Long questionId, List<String> tagNames) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow();

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
                .orElseThrow();
        String currentEmail = SecurityUtil.getCurrentEmail();

        Member currentMember = memberRepository.findByEmail(currentEmail)
                .orElseThrow(MemberNotFoundException::new);

        if (question.hasRecommendationFrom(currentMember)) {
            throw new QuestionAlreadyVotedException();
        }

        QuestionRecommend recommend = QuestionRecommend.builder()
                .type(type)
                .member(currentMember)
                .question(question)
                .build();

        recommend.applyRecommend();
        questionRepository.save(question);
        questionRecommendRepository.save(recommend);
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
