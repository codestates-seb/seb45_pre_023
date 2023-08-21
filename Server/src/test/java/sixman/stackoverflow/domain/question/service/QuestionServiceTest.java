package sixman.stackoverflow.domain.question.service;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.answer.repository.AnswerRepository;
import sixman.stackoverflow.domain.answer.service.AnswerService;
import sixman.stackoverflow.domain.answer.service.response.AnswerResponse;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.member.service.dto.response.MemberInfo;
import sixman.stackoverflow.domain.question.controller.QuestionController;
import sixman.stackoverflow.domain.question.controller.dto.QuestionCreateApiRequest;
import sixman.stackoverflow.domain.question.controller.dto.QuestionSortRequest;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.domain.question.service.response.QuestionDetailResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionResponse;
import sixman.stackoverflow.domain.question.service.response.QuestionTagResponse;
import sixman.stackoverflow.domain.questionrecommend.entity.QuestionRecommend;
import sixman.stackoverflow.domain.questiontag.QuestionTagRepository;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.domain.tag.repository.TagRepository;
import sixman.stackoverflow.global.entity.TypeEnum;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberBadCredentialsException;
import sixman.stackoverflow.global.exception.businessexception.questionexception.QuestionNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.tagexception.TagNotFoundException;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.testhelper.ServiceTest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static sixman.stackoverflow.domain.question.controller.dto.QuestionSortRequest.CREATED_DATE;

public class QuestionServiceTest extends ServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionTagRepository questionTagRepository;
    @Autowired
    private QuestionController questionController;


    @Test
    @DisplayName("질문 목록을 페이징 해서 10개의 리스트를 최신 순으로 조회한다.")
    public void getLatestQuestionsSortByCreatedDate() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        for (int i = 0; i < 10; i++) {
            Question question = createQuestionDetail(member, i);
            questionRepository.save(question);
        }

        Pageable pageable1 = PageRequest.of(0, 10, Sort.by(QuestionSortRequest.CREATED_DATE.getValue()).descending());

        setDefaultAuthentication(member.getMemberId());


        //when
        Page<QuestionResponse> result1 = questionService.getLatestQuestions(pageable1, null);


        // then

        assertThat(result1.getContent()).hasSize(10);

        // 최신순으로 정렬되었는지 확인
        List<QuestionResponse> questionList = result1.getContent();
        Instant previousCreatedDate = Instant.now();
        for (QuestionResponse question : questionList) {
            LocalDateTime currentLocalDateTime = question.getCreatedDate();
            Instant currentCreatedDate = currentLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();

            assertThat(currentCreatedDate).isBeforeOrEqualTo(previousCreatedDate);
            previousCreatedDate = currentCreatedDate;
        }
    }

    @Test
    @DisplayName("질문 목록을 페이징 해서 10개의 리스트를 조회수 순으로 조회한다.")
    public void getLatestQuestionsSortByViews() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        for (int i = 0; i < 10; i++) {
            Question question = createQuestionDetail(member, i);
            questionRepository.save(question);
        }

        Pageable pageable2 = PageRequest.of(0, 10, Sort.by(QuestionSortRequest.VIEWS.getValue()).descending());


        setDefaultAuthentication(member.getMemberId());


        //when
        Page<QuestionResponse> result2 = questionService.getLatestQuestions(pageable2, null);

        // then

        assertThat(result2.getContent()).hasSize(10);


        // 조회수로 정렬되었는지 확인
        List<QuestionResponse> questionList2 = result2.getContent();
        int previousViews = Integer.MAX_VALUE;
        for (QuestionResponse question : questionList2) {
            assertThat(question.getViews()).isLessThanOrEqualTo(previousViews);
            previousViews = question.getViews();
        }
    }

    @Test
    @DisplayName("질문 목록을 페이징 해서 10개의 리스트를 추천 순으로 조회한다.")
    public void getLatestQuestionsSortByRecommend() {
        // given
        Member member = createMember();
        memberRepository.save(member);

        for (int i = 0; i < 10; i++) {
            Question question = createQuestionDetail(member, i);
            questionRepository.save(question);
        }

        Pageable pageable3 = PageRequest.of(0, 10, Sort.by(QuestionSortRequest.RECOMMEND.getValue()).descending());

        setDefaultAuthentication(member.getMemberId());


        //when
        Page<QuestionResponse> result3 = questionService.getLatestQuestions(pageable3, null);

        // then

        assertThat(result3.getContent()).hasSize(10);

        // 추천수 정렬이 되었는지 확인
        List<QuestionResponse> questionList3 = result3.getContent();
        int previousRecommend = Integer.MAX_VALUE;
        for (QuestionResponse question : questionList3) {
            assertThat(question.getRecommend()).isLessThanOrEqualTo(previousRecommend);
            previousRecommend = question.getRecommend();
        }
    }

//    @Test
//    @DisplayName("입력받은 tagName에 해당하는 질문이 없으면 TagNotFoundException 예외 발생")
//    public void getLatestQuestionsTagException(){
//        //given
//        String nonExistentTagName = "nonExistentTag";
//
//        // when & then
//        assertThrows(TagNotFoundException.class, () -> {
//            questionService.getLatestQuestions(Pageable.unpaged(), nonExistentTagName);
//        });
//    }


//    @Test
//    @DisplayName("questionId를 받아서 해당 질문글을 조회한다.")
//    public void getQuestionById(){
//        // given
//        Member member = createMember();
//        Question question = createQuestionDetail(member,0);
//        Answer answer = createanswer(member, question);
//
//        Tag tag1 = createTag("tag1");
//        Tag tag2 = createTag("tag2");
//
//        QuestionTag questionTag1 = QuestionTag.builder()
//                .question(question)
//                .tag(tag1)
//                .build();
//
//        QuestionTag questionTag2 = QuestionTag.builder()
//                .question(question)
//                .tag(tag2)
//                .build();
//
//        question.setQuestionTags(List.of(questionTag1, questionTag2));
//
//        memberRepository.save(member);
//        questionRepository.save(question);
//        answerRepository.save(answer);
//        tagRepository.save(tag1);
//        tagRepository.save(tag2);
//
//        setDefaultAuthentication(member.getMemberId());
//
//        // when
//        QuestionDetailResponse result = questionService.getQuestionById(question.getQuestionId());
//
//        // then
//
//        assertThat(result).isNotNull();
//        assertThat(result.getQuestionId()).isEqualTo(question.getQuestionId());
//        assertThat(result.getTitle()).isEqualTo(question.getTitle());
//        assertThat(result.getDetail()).isEqualTo(question.getDetail());
//        assertThat(result.getExpect()).isEqualTo(question.getExpect());
//        assertThat(result.getMember().getMemberId()).isEqualTo(question.getMember().getMemberId());
//        assertThat(result.getViews()).isEqualTo(101);
//        assertThat(result.getRecommend()).isEqualTo(question.getRecommend());
//
//        // 태그 확인
//        assertThat(result.getTags()).isNotNull();
//        List<String> actualTags = result.getTags().stream()
//                .map(QuestionTagResponse::getTagName)
//                .collect(Collectors.toList());
//
//        List<String> expectedTags = question.getQuestionTags().stream()
//                .map(tag -> tag.getTag().getTagName())
//                .collect(Collectors.toList());
//
//        assertThat(actualTags).containsExactlyInAnyOrderElementsOf(expectedTags);
//
//        assertThat(result.getAnswer().getAnswers()).hasSize(1); // 답변이 1개인지 확인
//        assertThat(result.getCreatedDate()).isEqualTo(question.getCreatedDate());
//        assertThat(result.getUpdatedDate()).isEqualTo(question.getModifiedDate());
//    }


    @Test
    @DisplayName("생성된 question의 questionId를 반환한다.")
    void createQuestionTest(){
        //given
        Member member = createMember();
        Tag tag1 = createTag("tag1");
        Tag tag2 = createTag("tag2");
        memberRepository.save(member);
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        Question question = createQuestion(member);
        List<String> tagNames = List.of(tag1.getTagName(), tag2.getTagName());

        //when
        Long questionId = questionService.createQuestion(question, tagNames);

        //then
        Question findQuestion = questionRepository.findById(questionId).orElseThrow();
        assertThat(findQuestion.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findQuestion.getTitle()).isEqualTo(question.getTitle());
        assertThat(findQuestion.getDetail()).isEqualTo(question.getDetail());
        assertThat(findQuestion.getExpect()).isEqualTo(question.getExpect());
        assertThat(findQuestion.getViews()).isEqualTo(0);
        assertThat(findQuestion.getRecommend()).isEqualTo(0);
        assertThat(findQuestion.getQuestionTags()).hasSize(2);
    }

    @Test
    @DisplayName("questionId와 해당 질문글의 내용을 받아서 내용을 수정한다.")
    public void updateQuestion(){
        //given
        Member member = createMember();
        Tag tag1 = createTag("tag1");
        Tag tag2 = createTag("tag2");
        Tag tag3 = createTag("tag3");
        Question question = createQuestion(member);

        QuestionTag questionTag1 = QuestionTag.builder()
                .question(question)
                .tag(tag1)
                .build();

        QuestionTag questionTag2 = QuestionTag.builder()
                .question(question)
                .tag(tag2)
                .build();

        question.setQuestionTags(List.of(questionTag1, questionTag2));

        memberRepository.save(member);
        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);
        questionRepository.save(question);

        setDefaultAuthentication(member.getMemberId());

        String updateTitle = "updateTitle";
        String updateDetail = "updateDetail";
        String updateExpect = "updateExpect";
        List<String> updateTagNames = List.of(tag2.getTagName(), tag3.getTagName());

        //when
        questionService.updateQuestion(
                question.getQuestionId(),
                updateTitle,
                updateDetail,
                updateExpect,
                updateTagNames);

        //then
        Question findQuestion = questionRepository.findById(question.getQuestionId()).orElseThrow();
        assertThat(findQuestion.getTitle()).isEqualTo(updateTitle);
        assertThat(findQuestion.getDetail()).isEqualTo(updateDetail);
        assertThat(findQuestion.getExpect()).isEqualTo(updateExpect);
        assertThat(findQuestion.getQuestionTags().stream().map(
                questionTag -> questionTag.getTag().getTagName()
        )).containsAll(updateTagNames);


    }

    @Test
    @DisplayName("questionId를 받아서 해당 질문글을 삭제한다.")
    public void deleteQuestion(){

        //given
        Member member = createMember();
        Question question = createQuestion(member);

        memberRepository.save(member);
        questionRepository.save(question);

        setDefaultAuthentication(member.getMemberId());

        //when
        questionService.deleteQuestion(question.getQuestionId());

        //then
        Optional<Question> findQuestion = questionRepository.findById(question.getQuestionId());
        assertFalse(findQuestion.isPresent());
    }

    @Test
    @DisplayName("questionId를 받아서 해당 질문글을 추천한다.")
    public void addQuestionRecommendUpvote(){
        //given
        Member member = createMember();

        Question question = createQuestion(member);

        question.setQuestionRceommends(new ArrayList<>());


        memberRepository.save(member);
        questionRepository.save(question);

        setDefaultAuthentication(member.getMemberId());

        //when

        questionService.addQuestionRecommend(question.getQuestionId(), TypeEnum.UPVOTE);


        //then

        Question findQuestion = questionRepository.findById(question.getQuestionId()).orElseThrow();
        assertThat(findQuestion.getRecommend()).isEqualTo(1);

        assertThat(findQuestion.getQuestionRecommends()).hasSize(1);
    }

    @Test
    @DisplayName("질문을 추천하고 다시 추천을 누르면 취소가 되는지 테스트")
    public void addQuestionRecommendUpvotecancel(){
        //given
        Member member = createMember();

        Question question = createQuestionDetail(member,0);

        question.setQuestionRceommends(new ArrayList<>());


        memberRepository.save(member);
        questionRepository.save(question);

        setDefaultAuthentication(member.getMemberId());

        //when

        questionService.addQuestionRecommend(question.getQuestionId(), TypeEnum.UPVOTE);
        questionService.addQuestionRecommend(question.getQuestionId(), TypeEnum.UPVOTE);


        //then

        Question findQuestion = questionRepository.findById(question.getQuestionId()).orElseThrow();
        assertThat(findQuestion.getRecommend()).isEqualTo(0);

    }


    @Test
    @DisplayName("질문을 추천하고 비추천을 누르면 추천이 취소가 되고 비추천이 되는지 테스트")
    public void addQuestionRecommendUpvoteSwitching(){
        //given
        Member member = createMember();

        Question question = createQuestionDetail(member,0);

        question.setQuestionRceommends(new ArrayList<>());


        memberRepository.save(member);
        questionRepository.save(question);

        setDefaultAuthentication(member.getMemberId());

        //when

        questionService.addQuestionRecommend(question.getQuestionId(), TypeEnum.UPVOTE);
        questionService.addQuestionRecommend(question.getQuestionId(), TypeEnum.DOWNVOTE);


        //then

        Question findQuestion = questionRepository.findById(question.getQuestionId()).orElseThrow();
        assertThat(findQuestion.getRecommend()).isEqualTo(-1);

        assertThat(findQuestion.getQuestionRecommends()).hasSize(1);
    }

    @Test
    @DisplayName("로그인을 하지 않으면 질문을 등록할 수 없다.")
    public void createQuestionException(){
        // given
        Member member = createMember();
        Question question = createQuestion(member);

        memberRepository.save(member);
        questionRepository.save(question);

        QuestionCreateApiRequest createApiRequest = QuestionCreateApiRequest.builder()
                .title("Title")
                .detail("Detail")
                .expect("Expect")
                .tagNames(Arrays.asList("tag1", "tag2"))
                .build();


        // When & Then
        assertThrows(MemberBadCredentialsException.class, () -> {
            questionController.createQuestion(createApiRequest);
        });

    }

    @Test
    @DisplayName("다른 사람의 질문을 삭제할 수 없다.")
    public void DeleteMemberAccessDeniedException(){
        Member member = createMember();
        Member otherMember = createMember();
        Question question = createQuestion(member);

        memberRepository.save(member);
        memberRepository.save(otherMember);
        questionRepository.save(question);

        setDefaultAuthentication(otherMember.getMemberId());


        // when, then
        assertThatThrownBy(
                () -> questionService.deleteQuestion(question.getQuestionId()))
                .isInstanceOf(MemberAccessDeniedException.class)
                .hasMessage("접근 권한이 없습니다.");
    }

    @Test
    @DisplayName("다른 사람의 질문을 수정할 수 없다.")
    public void UpdateMemberAccessDeniedException(){
        Member member = createMember();
        Member otherMember = createMember();
        Question question = createQuestion(member);

        memberRepository.save(member);
        memberRepository.save(otherMember);
        questionRepository.save(question);

        setDefaultAuthentication(otherMember.getMemberId());

        String title = "update title";
        String detail = "update detail";
        String expect = "update expect";
        List<String> tags = Arrays.asList("tag1", "tag2");


        // when, then
        assertThatThrownBy(
                () -> questionService.updateQuestion(question.getQuestionId(),title, detail, expect, tags))
                .isInstanceOf(MemberAccessDeniedException.class)
                .hasMessage("접근 권한이 없습니다.");
    }

    @Test
    @DisplayName("로그인을 하지 않으면 질문 추천을 할 수 없다.")
    public void addQuestionRecommendException() {
        // Given
        Member member = createMember();
        Question question = createQuestionDetail(member,0);

        memberRepository.save(member);
        questionRepository.save(question);


        // When & Then
        assertThrows(MemberBadCredentialsException.class, () -> {
            questionService.addQuestionRecommend(question.getQuestionId(), TypeEnum.UPVOTE);
        });
    }


    private Question createQuestion(Member member) {
        return Question.builder()
                .member(member)
                .detail("detail")
                .title("title")
                .expect("expect")
                .questionTags(new ArrayList<>())
                .build();
    }

    private Question createQuestionDetail(Member member, int num) {

        return Question.builder()
                .member(member)
                .detail("detail")
                .title("title")
                .expect("expect")
                .views(100+num)
                .recommend(num)
                .questionRecommends(new ArrayList<>())
                .questionTags(new ArrayList<>())
                .answers(new ArrayList<>())
                .build();
    }


}
