package sixman.stackoverflow.domain.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.member.service.dto.request.*;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.exception.businessexception.emailexception.EmailAuthNotAttemptException;
import sixman.stackoverflow.global.exception.businessexception.emailexception.EmailAuthNotCompleteException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberDuplicateException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberNotFoundException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberPasswordException;
import sixman.stackoverflow.global.testhelper.ServiceTest;
import sixman.stackoverflow.module.email.service.MailService;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MemberServiceTest extends ServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired MailService mailService;


    @TestFactory
    @DisplayName("email, nickname, password 를 받아 회원가입을 한다.")
    Collection<DynamicTest> signup() {
        //given
        String email = "test@test.com";
        String nickname = "test";
        String password = "1234abcd!";

        MemberCreateServiceRequest request = createSignupRequest(email, nickname, password);

        setRedisServiceSaveValuesReturn();

        setRedisServiceGetValuesReturn("true");

        //when
        Long memberId = memberService.signup(request);

        //then
        Member member = memberRepository.findById(memberId).orElseThrow();

        return List.of(
                dynamicTest("해당 email, nickname, password 로 저장된다.", () -> {
                    //then
                    assertThat(member.getEmail()).isEqualTo(email);
                    assertThat(member.getNickname()).isEqualTo(nickname);
                    assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();
                }),
                dynamicTest("권한이 USER 로 저장된다.", () -> {
                    //then
                    assertThat(member.getAuthority()).isEqualTo(Authority.ROLE_USER);
                }),
                dynamicTest("myInfo 가 함께 저장된다.", () -> {
                    //then
                    assertThat(member.getMyInfo()).isNotNull();
                })
        );
    }


    @Test
    @DisplayName("회원가입 시 중복되는 email 이 있으면 MemberDuplicateException 을 발생시킨다.")
    void signupMemberDuplicateException() {
        //given
        Member member = createAndSaveMember();

        MemberCreateServiceRequest request = createSignupRequest(member.getEmail(), "new nickname", "1234abcd!!!");

        //when & then
        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(MemberDuplicateException.class)
                .hasMessageContaining("이미 존재하는 회원 이메일입니다.");
    }



    @Test
    @DisplayName("회원가입 시 아직 이메일 인증이 완료되지 않으면 EmailAuthNotCompleteException 을 발생시킨다.")
    void signupEmailAuthNotCompleteException() {
        //given
        MemberCreateServiceRequest request = createSignupRequest("test@google.com", "nickname", "1234abcd!!!");

        setRedisServiceGetValuesReturn("not true");

        //when & then
        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(EmailAuthNotCompleteException.class)
                .hasMessageContaining("이메일 인증이 완료되지 않았습니다.");
    }

    @Test
    @DisplayName("nickname, myIntro, title, location, accounts 를 받아서 member 객체를 수정한다.")
    void updateMember() {
        //given
        Member member = createAndSaveMember();

        MemberUpdateServiceRequest request = createUpdateRequest(member);

        //when
        memberService.updateMember(member.getMemberId(), request);

        //then
        Member updatedMember = memberRepository.findById(member.getMemberId()).orElseThrow();
        assertThat(updatedMember.getNickname()).isEqualTo(request.getNickname());
        assertThat(updatedMember.getMyInfo().getMyIntro()).isEqualTo(request.getMyIntro());
        assertThat(updatedMember.getMyInfo().getTitle()).isEqualTo(request.getTitle());
        assertThat(updatedMember.getMyInfo().getLocation()).isEqualTo(request.getLocation());
        assertThat(updatedMember.getMyInfo().getAccounts()).hasSize(2);
    }



    @Test
    @DisplayName("다른 멤버의 정보를 업데이트 하려고 하면 MemberAccessDeniedException 을 발생시킨다.")
    void updateMemberException() {
        //given
        Member member = createAndSaveMember();
        Member otherMember = createAndSaveMember();

        MemberUpdateServiceRequest request = createUpdateRequest(otherMember);

        //when & then
        assertThatThrownBy(() -> memberService.updateMember(member.getMemberId(), request))
                .isInstanceOf(MemberAccessDeniedException.class)
                .hasMessageContaining("접근 권한이 없습니다.");
    }

    @Test
    @DisplayName("이전 비밀번호와 새 비밀번호를 받아서 비밀번호를 수정한다.")
    void updatePassword() {
        //given
        String password = "1234abcd!";
        String newPassword = "1234abcd!!";
        String encodedPassword = passwordEncoder.encode(password);
        Member member = createAndSaveMember(encodedPassword);

        MemberPasswordUpdateServiceRequest request = createPasswordUpdateRequest(member, password, newPassword);

        //when
        memberService.updatePassword(member.getMemberId(), request);

        //then
        assertThat(passwordEncoder.matches(newPassword, member.getPassword())).isTrue();
    }



    @Test
    @DisplayName("다른 멤버의 비밀번호를 수정하려고 하면 MemberAccessDeniedException 을 발생시킨다.")
    void updatePasswordAccessDeniedException() {
        //given
        String otherPassword = "1234abcd!";
        String newOtherPassword = "1234abcd!!";
        String encodedPassword = passwordEncoder.encode(otherPassword);

        Member member = createAndSaveMember();
        Member otherMember = createAndSaveMember(encodedPassword);

        MemberPasswordUpdateServiceRequest request = createPasswordUpdateRequest(otherMember, otherPassword, newOtherPassword);

        //when & then
        assertThatThrownBy(() -> memberService.updatePassword(member.getMemberId(), request))
                .isInstanceOf(MemberAccessDeniedException.class)
                .hasMessageContaining("접근 권한이 없습니다.");

    }

    @Test
    @DisplayName("비밀번호 변경 시 이전 비밀번호가 맞지 않으면 MemberPasswordException 을 발생시킨다.")
    void updatePasswordException() {
        //given
        String password = "1234abcd!";
        String newPassword = "1234abcd!!";
        String encodedPassword = passwordEncoder.encode(password);

        Member member = createAndSaveMember(encodedPassword);

        MemberPasswordUpdateServiceRequest request = createPasswordUpdateRequest(member, password + "wrong password", newPassword);

        //when & then
        assertThatThrownBy(() -> memberService.updatePassword(member.getMemberId(), request))
                .isInstanceOf(MemberPasswordException.class)
                .hasMessageContaining("비밀번호를 확인해주세요.");
    }

    @Test
    @DisplayName(" member email 인증을 완료하고 비밀번호를 변경할 수 있다.")
    void findPassword() {
        //given
        Member member = createAndSaveMember();

        String newPassword = "1234abcd!!";
        MemberFindPasswordServiceRequest request = createFindPasswordRequest(member.getEmail(), newPassword);

        setRedisServiceGetValuesReturn("true");

        //when
        memberService.findPassword(request);

        //then
        Member updatedMember = memberRepository.findById(member.getMemberId()).orElseThrow();
        assertThat(passwordEncoder.matches(newPassword, updatedMember.getPassword())).isTrue();
    }


    @Test
    @DisplayName("member 가 disabled 상태일 때 member email 인증을 완료하고 비밀번호를 바꾸면 member 의 enable 로 변경한다.")
    void findPasswordAndChangeEnable() {
        //given
        Member member = createAndSaveMemberDisable();

        String password = "1234abcd!!";
        MemberFindPasswordServiceRequest request = createFindPasswordRequest(member.getEmail(), password);

        setRedisServiceGetValuesReturn("true");

        //when
        memberService.findPassword(request);

        //then
        Member updatedMember = memberRepository.findById(member.getMemberId()).orElseThrow();
        assertThat(updatedMember.isEnabled()).isTrue();
    }


    @Test
    @DisplayName("member password 를 찾을 때 email 인증이 완료되지 않았으면 EmailAuthNotCompleteException 을 발생시킨다.")
    void findPasswordException() {
        //given
        Member member = createAndSaveMember();

        String newPassword = "1234abcd!!";
        MemberFindPasswordServiceRequest request = createFindPasswordRequest(member.getEmail(), newPassword);

        setRedisServiceGetValuesReturn("not true");

        //when & then
        assertThatThrownBy(() -> memberService.findPassword(request))
                .isInstanceOf(EmailAuthNotCompleteException.class)
                .hasMessageContaining("이메일 인증이 완료되지 않았습니다.");
    }

    @Test
    @DisplayName("member 삭제 시 enable 을 false 로 변경해서 비활성화 한다.")
    void deleteMember() {
        //given
        String password = "1234abcd!";
        String encodedPassword = passwordEncoder.encode(password);
        Member member = createAndSaveMember(encodedPassword);

        //when
        memberService.deleteMember(member.getMemberId(), member.getMemberId());

        //then
        Member deletedMember = memberRepository.findById(member.getMemberId()).orElseThrow();
        assertThat(deletedMember.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("다른 멤버를 삭제하려고 하면 MemberAccessDeniedException 을 발생시킨다.")
    void deleteMemberException() {
        //given

        String password = "1234abcd!";
        String encodedPassword = passwordEncoder.encode(password);
        Member otherMember = createAndSaveMember(encodedPassword);
        Member member = createAndSaveMember();


        //when
        assertThatThrownBy(() -> memberService.deleteMember(member.getMemberId(), otherMember.getMemberId())) // 다른 멤버를 삭제
                .isInstanceOf(MemberAccessDeniedException.class)
                .hasMessageContaining("접근 권한이 없습니다.");
    }

    @Test
    @DisplayName("memberId 를 통해 해당 멤버의 정보, 멤버의 게시물, 댓글, 태그 정보를 모두 가져올 수 있다.")
    void getMemberInfo() {
        //given
        Member member = createAndSaveMember();

        //when
        MemberResponse response = memberService.getMember(member.getMemberId());

        //then
        assertThat(response.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(response.getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getNickname()).isEqualTo(member.getNickname());
        assertThat(response.getMyIntro()).isEqualTo(member.getMyInfo().getMyIntro());
        assertThat(response.getTitle()).isEqualTo(member.getMyInfo().getTitle());
        assertThat(response.getLocation()).isEqualTo(member.getMyInfo().getLocation());
        assertThat(response.getAccounts()).isEqualTo(member.getMyInfo().getAccounts());
    }

    @TestFactory
    @DisplayName("memberId 를 통해 해당 멤버의 게시물 정보를 가져온다.")
    Collection<DynamicTest> getMemberQuestion() {
        //given
        Member member = createAndSaveMember();

        Tag tag1 = createAndSaveTag("tag1");
        Tag tag2 = createAndSaveTag("tag2");

        createAndSaveQuestionsWithTag(member, 10, tag1, tag2);

        //when
        MemberResponse response = memberService.getMember(member.getMemberId());

        return List.of(
                dynamicTest("게시물 정보 중 첫 페이지를 가져온다.", () -> {
                    //then
                    assertThat(response.getQuestion().getQuestions()).hasSize(5);
                    assertThat(response.getQuestion().getPageInfo().getPage()).isEqualTo(1);
                    assertThat(response.getQuestion().getPageInfo().getSize()).isEqualTo(5);
                    assertThat(response.getQuestion().getPageInfo().getTotalSize()).isEqualTo(10);
                    assertThat(response.getQuestion().getPageInfo().getTotalPage()).isEqualTo(2);
                }),
                dynamicTest("게시물 정보 중 작성한 모든 태그목록을 가져온다.", () -> {
                    //then
                    assertThat(response.getTags()).hasSize(2)
                            .extracting("tagName")
                            .containsExactlyInAnyOrder("tag1", "tag2");
                })
        );
    }

    @Test
    @DisplayName("memberId 를 통해 해당 멤버의 답변 정보 중 첫 페이지를 가져온다.")
    void getMemberAnswer() {
        //given
        Member member = createAndSaveMember();
        Member otherMember = createAndSaveMember();

        List<Question> othersQuestions = createAndSaveQuestionsWithTag(otherMember, 10);

        othersQuestions.forEach(
                question -> createAndSaveAnswer(member, question)
        );

        //when
        MemberResponse response = memberService.getMember(member.getMemberId());

        //then

        assertThat(response.getAnswer().getAnswers()).hasSize(5);
        assertThat(response.getAnswer().getPageInfo().getPage()).isEqualTo(1);
        assertThat(response.getAnswer().getPageInfo().getSize()).isEqualTo(5);
        assertThat(response.getAnswer().getPageInfo().getTotalSize()).isEqualTo(10);
        assertThat(response.getAnswer().getPageInfo().getTotalPage()).isEqualTo(2);
    }

    @Test
    @DisplayName("memberId 와 페이지를 통해 해당 멤버의 최신 순 게시물을 가져온다.")
    void getMemberQuestionOnly() {
        //given
        Member member = createAndSaveMember();

        createAndSaveQuestionsWithTag(member, 10);

        //when
        Page<MemberResponse.MemberQuestion> memberQuestion =
                memberService.getMemberQuestion(member.getMemberId(), 0, 5);

        //then
        assertThat(memberQuestion.getContent()).hasSize(5);
        assertThat(memberQuestion.getNumber()).isEqualTo(0);
        assertThat(memberQuestion.getSize()).isEqualTo(5);
        assertThat(memberQuestion.getTotalElements()).isEqualTo(10);
        assertThat(memberQuestion.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("memberId 와 페이지를 통해 해당 멤버의 최신 순 답변 목록을 가져온다.")
    void getMemberAnswerOnly() {
        //given
        Member member = createAndSaveMember();
        Member otherMember = createAndSaveMember();

        List<Question> othersQuestions = createAndSaveQuestionsWithTag(otherMember, 10);

        othersQuestions.forEach(
                question -> createAndSaveAnswer(member, question)
        );

        //when
        Page<MemberResponse.MemberAnswer> memberAnswer
                = memberService.getMemberAnswer(member.getMemberId(), 0, 5);

        //then
        assertThat(memberAnswer.getContent()).hasSize(5);
        assertThat(memberAnswer.getNumber()).isEqualTo(0);
        assertThat(memberAnswer.getSize()).isEqualTo(5);
        assertThat(memberAnswer.getTotalElements()).isEqualTo(10);
        assertThat(memberAnswer.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("s3 버킷에 image 를 업로드하고 업로드한 이미지의 presigned url 을 반환한다.")
    void updateImage() {
        //given
        Member member = createAndSaveMember();

        MultipartFile image = createMockFile("image", "image/png");

        setS3ServiceUploadReturn("https://image.png");

        //when
        String imageUrl = memberService.updateImage(member.getMemberId(), member.getMemberId(), image);

        //then
        assertThat(imageUrl).isEqualTo("https://image.png");
    }

    @Test
    @DisplayName("s3 버킷에 image 를 업로드 시 다른 member 의 이미지 업로드를 요청하면 MemberAccessDeniedException 이 발생한다.")
    void updateImageException() {
        //given
        Member member = createAndSaveMember();
        Member otherMember = createAndSaveMember();

        MultipartFile image = createMockFile("image", "image/png");

        setS3ServiceUploadReturn("https://image.png");

        //when & then
        assertThatThrownBy(() -> memberService.updateImage(member.getMemberId(), otherMember.getMemberId(), image))
                .isInstanceOf(MemberAccessDeniedException.class);
    }

    @Test
    @DisplayName("s3 버킷에서 해당 멤버의 이미지를 삭제하고 Myinfo 의 image 를 null 로 만든다.")
    void deleteImage() {
        //given
        Member member = createAndSaveMember();

        willDoNothing().given(s3Service).deleteImage(anyString());

        //when
        memberService.deleteImage(member.getMemberId(), member.getMemberId());

        //then
        assertThat(member.getMyInfo().getImage()).isNull();
    }

    @Test
    @DisplayName("멤버의 이미지를 삭제 시 다른 멤버의 이미지를 삭제하면 MemberAccessDeniedException 이 발생한다.")
    void deleteImageException() {
        //given
        Member member = createAndSaveMember();
        Member otherMember = createAndSaveMember();

        willDoNothing().given(s3Service).deleteImage(anyString());

        //when & then
        assertThatThrownBy(() -> memberService.deleteImage(member.getMemberId(), otherMember.getMemberId()))
                .isInstanceOf(MemberAccessDeniedException.class);
    }

    @Test
    @DisplayName("회원가입을 위한 이메일 인증을 시도하면 이메일로 인증 코드를 보내고 redis 에 저장한다.")
    void sendCodeToEmail() {
        //given
        String email = "test@google.com";

        setEmailSenderCreateMessageReturn();

        //when
        memberService.sendSignupCodeToEmail(email);

        //then
        //저장 로직 호출 확인
        verify(redisService, times(1)).saveValues(anyString(), anyString(), any(Duration.class));
        //이메일 전송 로직 호출 확인
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("회원가입을 위한 이메일 인증 시도 시 중복된 이메일이면 MemberDuplicateException 이 발생한다.")
    void sendCodeToEmailException() {
        //given
        Member member = createAndSaveMember();

        //when & then
        assertThatThrownBy(() -> memberService.sendSignupCodeToEmail(member.getEmail()))
                .isInstanceOf(MemberDuplicateException.class)
                .hasMessageContaining("이미 존재하는 회원 이메일입니다.");
    }

    @Test
    @DisplayName("비밀번호를 찾기 위해 이메일 인증을 시도하면 이메일로 인증 코드를 보내고 redis 에 저장한다.")
    void sendFindPasswordCodeToEmail() {
        //given
        Member member = createAndSaveMember();

        setEmailSenderCreateMessageReturn();

        //when
        memberService.sendFindPasswordCodeToEmail(member.getEmail());

        //then
        verify(redisService, times(1)).saveValues(anyString(), anyString(), any(Duration.class)); //저장 로직 호출
        verify(emailSender, times(1)).send(any(MimeMessage.class)); //이메일 전송 로직 호출
    }



    @Test
    @DisplayName("비밀번호를 찾기 위해 이메일 인증을 시도 시 이메일이 가입되어있지 않으면 MemberNotFoundException 이 발생한다.")
    void sendFindPasswordCodeToEmailException() {
        //given
        String email = "notSavedEmail@test.com";

        setEmailSenderCreateMessageReturn();

        //when & then
        assertThatThrownBy(() -> memberService.sendFindPasswordCodeToEmail(email))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("존재하지 않는 회원입니다.");
    }

    @Test
    @DisplayName("이메일로 온 코드를 인증할 때 redis 에 저장된 코드와 일치하면 true 를 반환한다.")
    void checkCode() {
        //given
        String email = "test@google.com";
        String code = "123456";

        setRedisServiceGetValuesReturn(code);

        //when
        boolean result = memberService.checkCode(email, code);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이메일로 온 코드를 인증할 때 redis 에 저장된 코드와 일치하지 않으면 false 를 반환한다.")
    void checkCodeFalse() {
        //given
        String email = "test@google.com";
        String code = "123456";

        setRedisServiceGetValuesReturn("654321");

        //when
        boolean result = memberService.checkCode(email, code);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("이메일로 온 코드를 인증할 때 redis 에 저장된 코드가 없으면 EmailAuthNotAttemptException 이 발생한다.")
    void checkCodeException() {
        //given
        String email = "test@google.com";
        String code = "123456";

        setRedisServiceGetValuesReturn(null);

        //when & then
        assertThatThrownBy(() -> memberService.checkCode(email, code))
                .isInstanceOf(EmailAuthNotAttemptException.class)
                .hasMessageContaining("이메일 인증을 먼저 시도해주세요.");
    }

    private Question createQuestion(Member member) {
        return Question.builder()
                .title("title")
                .detail("detail")
                .expect("expect")
                .member(member)
                .build();
    }

    private List<Question> createQuestions(Member member, int count){
        List<Question> questions = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            questions.add(createQuestion(member));
        }
        return questions;
    }

    private Answer createAnswer(Member member, Question question) {
        return Answer.builder()
                .content("content")
                .member(member)
                .question(question)
                .build();
    }

    private QuestionTag createQuestionTag(Question question, Tag tag){
        return QuestionTag.builder()
                .question(question)
                .tag(tag)
                .build();
    }

    private void setRedisServiceGetValuesReturn(String value) {
        given(redisService.getValues(anyString())).willReturn(value);
    }

    private void setRedisServiceSaveValuesReturn() {
        willDoNothing()
                .given(redisService)
                .saveValues(anyString(), anyString(), any(Duration.class));
    }

    private void setS3ServiceUploadReturn(String url) {
        given(s3Service.uploadAndGetUrl(anyString(), any(MultipartFile.class)))
                .willReturn(url);
    }

    private void setEmailSenderCreateMessageReturn() {
        given(emailSender.createMimeMessage())
                .willReturn(new MimeMessage(Session.getInstance(new Properties())));
    }

    private Member createAndSaveMember() {
        Member member = createMember();

        memberRepository.save(member);
        return member;
    }

    private Member createAndSaveMember(String password) {
        Member member = createMember(password);

        memberRepository.save(member);
        return member;
    }

    private Member createAndSaveMemberDisable() {
        Member member = createMemberDisable();

        memberRepository.save(member);
        return member;
    }

    private Tag createAndSaveTag(String name) {
        Tag tag = createTag(name);

        em.persist(tag);
        return tag;
    }

    private MemberCreateServiceRequest createSignupRequest(String email, String nickname, String password) {
        return MemberCreateServiceRequest.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();
    }

    private MemberUpdateServiceRequest createUpdateRequest(Member member) {
        return MemberUpdateServiceRequest.builder()
                .updateMemberId(member.getMemberId())
                .nickname("new nickname")
                .myIntro("new myIntro")
                .title("new title")
                .location("new location")
                .accounts(List.of("account1", "account2"))
                .build();
    }

    private MemberPasswordUpdateServiceRequest createPasswordUpdateRequest(Member member, String password, String newPassword) {
        return MemberPasswordUpdateServiceRequest.builder()
                .updateMemberId(member.getMemberId())
                .password(password)
                .newPassword(newPassword)
                .build();
    }

    private MemberFindPasswordServiceRequest createFindPasswordRequest(String email, String password) {
        return MemberFindPasswordServiceRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    private List<Question> createAndSaveQuestionsWithTag(Member member, int count, Tag... tags) {

        List<Question> questions = createQuestions(member, count);

        questions.forEach(question -> {
            em.persist(question);
            Arrays.stream(tags).forEach(tag -> em.persist(createQuestionTag(question, tag)));
        });

        return questions;
    }

    private Answer createAndSaveAnswer(Member member, Question question) {

        Answer answer = createAnswer(member, question);

        em.persist(answer);
        return answer;
    }

    private MockMultipartFile createMockFile(String fileName, String contentType) {
        return new MockMultipartFile(
                fileName,
                fileName + contentType.split("/")[1],
                contentType,
                fileName.getBytes());
    }
}