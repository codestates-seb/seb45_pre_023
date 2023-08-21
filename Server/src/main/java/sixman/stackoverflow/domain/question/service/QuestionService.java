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
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberBadCredentialsException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.questionexception.QuestionNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.tagexception.TagNotFoundException;
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

    public Page<QuestionResponse> getLatestQuestions(Pageable pageable, String tagName) {
        if (tagName == null) {
            return questionRepository.findAll(pageable).map(QuestionResponse::of);
        }
        tagRepository.findByTagName(tagName) //tag 가 존재하는지 확인
                .orElseThrow(TagNotFoundException::new);

        Page<Question> questions = questionRepository.findAllByTag(pageable, tagName); //tag 로 페이징
        return questions.map(QuestionResponse::of);
    }

    public QuestionDetailResponse getQuestionById(Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findByQuestionId(questionId);

        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();

            // 조회수 증가 로직 추가
            question.setViews(question.getViews() + 1);
            questionRepository.save(question);

            List<QuestionTag> questionTags = question.getQuestionTags();

            // 태그가 없는 경우 예외 처리
            if (questionTags.isEmpty()) {
                throw new TagNotFoundException();
            }

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


    public Long createQuestion(Question question, List<String> tagNames) {
        if(tagNames.isEmpty()){
            throw new TagNotFoundException();
        }

        for(String tag : tagNames){
            Optional<Tag> verifiedTag = tagRepository.findByTagName(tag);
            if(!verifiedTag.isPresent()){
                throw new TagNotFoundException();
            }
        }
        List<Tag> tags = tagRepository.findAllByTagNameIn(tagNames);

        List<QuestionTag> questionTags = tags.stream()
                .map(tag -> QuestionTag.createQuestionTag(question, tag))
                .collect(Collectors.toList());

        question.setQuestionTags(questionTags);

        Question savedQuestion = questionRepository.save(question);
        return savedQuestion.getQuestionId();
    }


    public Question updateQuestion(Long questionId, String title, String detail, String expect, List<String> tagNames) {
        Question existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(QuestionNotFoundException::new);

        if (SecurityUtil.isLogin()) {

            Long loggedInUserId = SecurityUtil.getCurrentId();

            Optional<Member> currentMemberOptional = memberRepository.findById(loggedInUserId);

            if (currentMemberOptional.isEmpty()) {
                throw new MemberNotFoundException();
            }
            Long questionAuthorId = questionRepository.findMemberIdByQuestionId(questionId);

            if (!loggedInUserId.equals(questionAuthorId)) {
                throw new MemberAccessDeniedException();
            }


            for (String tagName : tagNames) {
                Optional<Tag> tagOptional = tagRepository.findByTagName(tagName);
                if (tagOptional.isEmpty()) {
                    throw new TagNotFoundException();
                }
            }

            // 기존에 연결된 태그들을 가져와서 삭제할 수 있도록 처리
            List<QuestionTag> existingTags = existingQuestion.getQuestionTags();
            List<QuestionTag> tagsToRemove = new ArrayList<>();

            for (QuestionTag tag : existingTags) {
                if (!tagNames.contains(tag.getTag().getTagName())) {
                    tagsToRemove.add(tag);
                }
            }

            existingTags.removeAll(tagsToRemove);

            // 새로운 태그들을 가져와서 추가할 수 있도록 처리
            List<Tag> newTags = tagRepository.findAllByTagNameIn(tagNames);
            List<QuestionTag> newQuestionTags = newTags.stream()
                    .filter(tag -> existingTags.stream().noneMatch(questionTag -> questionTag.getTag().equals(tag)))
                    .map(tag -> QuestionTag.createQuestionTag(existingQuestion, tag))
                    .collect(Collectors.toList());
            existingTags.addAll(newQuestionTags);

            existingQuestion.setTitle(title);
            existingQuestion.setDetail(detail);
            existingQuestion.setExpect(expect);

            return questionRepository.save(existingQuestion);
        }else{
            throw new MemberBadCredentialsException();
        }
    }

    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(QuestionNotFoundException::new);

        if (SecurityUtil.isLogin()) {
            Long loggedInUserId = SecurityUtil.getCurrentId();

            Optional<Member> currentMemberOptional = memberRepository.findById(loggedInUserId);

            if (currentMemberOptional.isEmpty()) {
                throw new MemberNotFoundException();
            }

            Long questionAuthorId = questionRepository.findMemberIdByQuestionId(questionId);

            if (!loggedInUserId.equals(questionAuthorId)) {
                throw new MemberAccessDeniedException();
            }
            questionRepository.delete(question);
        }else{
            throw new MemberBadCredentialsException();
        }
    }



    public void addQuestionRecommend(Long questionId, TypeEnum type) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(QuestionNotFoundException::new);

        if (SecurityUtil.isLogin()) {

            Long currentId = SecurityUtil.getCurrentId();

            Member currentMember = memberRepository.findById(currentId)
                    .orElseThrow(MemberNotFoundException::new);

            Optional<QuestionRecommend> existingRecommend = question.getQuestionRecommends().stream()
                    .filter(recommend -> recommend.getMember().equals(currentMember))
                    .findFirst();

            if (existingRecommend.isPresent()) {
                QuestionRecommend recommend = existingRecommend.get();

                // 같은 타입의 추천을 취소하거나 반대 타입의 추천으로 변경
                if (type == recommend.getType()) {
                    question.getQuestionRecommends().remove(recommend);
                } else {
                    recommend.setType(type); // 추천 타입 변경
                }
            } else {
                // 새로운 추천 또는 비추천 추가
                QuestionRecommend newRecommend = QuestionRecommend.builder()
                        .type(type)
                        .member(currentMember)
                        .question(question)
                        .build();
                question.getQuestionRecommends().add(newRecommend);
            }

            // 추천 정보 변경 후 추천수 업데이트
            question.applyRecommend();
            questionRepository.save(question);
        }else{
            throw new MemberBadCredentialsException();
        }
    }
}
