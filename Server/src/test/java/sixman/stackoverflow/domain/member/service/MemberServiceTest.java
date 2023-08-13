package sixman.stackoverflow.domain.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import sixman.stackoverflow.global.exception.businessexception.emailexception.EmailAuthNotCompleteException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberDuplicateException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberPasswordException;
import sixman.stackoverflow.global.testhelper.ServiceTest;
import sixman.stackoverflow.module.email.service.MailService;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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


    @Test
    @DisplayName("email, nickname, password 를 받아 회원가입을 한다.")
    void signup() {
        //given
        String email = "test@test.com";
        String nickname = "test";
        String password = "1234abcd!";

        MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();

        willDoNothing()
                .given(redisService)
                .saveValues(anyString(), anyString(), any(Duration.class));

        given(redisService.getValues(anyString())).willReturn("true");

        //when
        Long memberId = memberService.signup(request);

        //then
        Member member = memberRepository.findById(memberId).orElseThrow();
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getNickname()).isEqualTo(nickname);
        assertThat(member.getPassword()).isNotNull();
        assertThat(member.getAuthority()).isEqualTo(Authority.ROLE_USER);
        assertThat(member.getMyInfo()).isNotNull();
    }

    @Test
    @DisplayName("회원가입 시 중복되는 email 이 있으면 MemberDuplicateException 을 발생시킨다.")
    void signupMemberDuplicateException() {
        //given
        Member member = createMember();

        memberRepository.save(member);

        MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
                .email(member.getEmail()) // 중복되는 email
                .nickname("new nickname")
                .password("1234abcd!!!")
                .build();


        //when & then
        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(MemberDuplicateException.class)
                .hasMessageContaining("이미 존재하는 회원 이메일입니다.");
    }

    @Test
    @DisplayName("회원가입 시 아직 이메일 인증이 완료되지 않으면 EmailAuthNotCompleteException 을 발생시킨다.")
    void signupEmailAuthNotCompleteException() {
        //given
        MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
                .email("test@google.com")
                .nickname("nickname")
                .password("1234abcd!!!")
                .build();

        given(redisService.getValues(anyString())).willReturn("code12");

        //when & then
        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(EmailAuthNotCompleteException.class)
                .hasMessageContaining("이메일 인증이 완료되지 않았습니다.");
    }

    @Test
    @DisplayName("nickname, myIntro 를 받아서 member 객체를 수정한다.")
    void updateMember() {
        //given
        Member member = createMember();

        memberRepository.save(member);

        MemberUpdateServiceRequest request = MemberUpdateServiceRequest.builder()
                .updateMemberId(member.getMemberId())
                .nickname("new nickname")
                .myIntro("new myIntro")
                .build();

        //when
        memberService.updateMember(member.getMemberId(), request);

        //then
        Member updatedMember = memberRepository.findById(member.getMemberId()).orElseThrow();
        assertThat(updatedMember.getNickname()).isEqualTo(request.getNickname());
        assertThat(updatedMember.getMyInfo().getMyIntro()).isEqualTo(request.getMyIntro());
    }

    @Test
    @DisplayName("다른 멤버의 정보를 업데이트 하려고 하면 MemberAccessDeniedException 을 발생시킨다.")
    void updateMemberException() {
        //given
        Member member = createMember();
        Member otherMember = createMember();

        memberRepository.save(member);
        memberRepository.save(otherMember);

        MemberUpdateServiceRequest request = MemberUpdateServiceRequest.builder()
                .updateMemberId(otherMember.getMemberId()) // 다른 멤버의 정보를 업데이트
                .nickname("new nickname")
                .myIntro("new myIntro")
                .build();

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
        Member member = createMember(encodedPassword);

        memberRepository.save(member);

        MemberPasswordUpdateServiceRequest request = MemberPasswordUpdateServiceRequest.builder()
                .updateMemberId(member.getMemberId())
                .password(password)
                .newPassword(newPassword)
                .build();

        //when
        memberService.updatePassword(member.getMemberId(), request);

        //then
        assertThat(passwordEncoder.matches(newPassword, member.getPassword())).isTrue();
    }

    @Test
    @DisplayName("다른 멤버의 비밀번호를 수정하려고 하면 MemberAccessDeniedException 을 발생시킨다.")
    void updatePasswordAccessDeniedException() {
        //given
        String password = "1234abcd!";
        String newPassword = "1234abcd!!";
        String encodedPassword = passwordEncoder.encode(password);
        Member member = createMember();
        Member otherMember = createMember(encodedPassword);

        memberRepository.save(member);
        memberRepository.save(otherMember);

        MemberPasswordUpdateServiceRequest request = MemberPasswordUpdateServiceRequest.builder()
                .updateMemberId(otherMember.getMemberId()) // 다른 멤버의 비밀번호를 수정
                .password(password)
                .newPassword(newPassword)
                .build();

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
        Member member = createMember(encodedPassword);

        memberRepository.save(member);

        MemberPasswordUpdateServiceRequest request = MemberPasswordUpdateServiceRequest.builder()
                .updateMemberId(member.getMemberId())
                .password(password + "wrong password") // 이전 비밀번호가 틀림
                .newPassword(newPassword)
                .build();

        //when & then
        assertThatThrownBy(() -> memberService.updatePassword(member.getMemberId(), request))
                .isInstanceOf(MemberPasswordException.class)
                .hasMessageContaining("비밀번호를 확인해주세요.");
    }

    @Test
    @DisplayName("member email 인증이 완료되면 member 의 비밀번호를 변경할 수 있다.")
    void findPassword() {
        //given
        Member member = createMember();
        memberRepository.save(member);

        String email = member.getEmail();
        String password = "1234abcd!!";
        String code = "abcd12";

        given(redisService.getValues(anyString())).willReturn("true");

        MemberFindPasswordServiceRequest request = MemberFindPasswordServiceRequest.builder()
                .email(email)
                .password(password)
                .code(code)
                .build();

        //when
        memberService.findPassword(request);

        //then
        Member updatedMember = memberRepository.findById(member.getMemberId()).orElseThrow();
        assertThat(passwordEncoder.matches(password, updatedMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("member password 를 찾을 때 email 인증이 완료되지 않았으면 EmailAuthNotCompleteException 을 발생시킨다.")
    void findPasswordException() {
        //given
        Member member = createMember();
        memberRepository.save(member);

        String email = member.getEmail();
        String password = "1234abcd!!";
        String code = "abcd12";

        given(redisService.getValues(anyString())).willReturn("abcd12"); //인증이 완료되지 않았으면 저장된 코드를 반환함.

        MemberFindPasswordServiceRequest request = MemberFindPasswordServiceRequest.builder()
                .email(email)
                .password(password)
                .code(code)
                .build();

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
        Member member = createMember(encodedPassword);

        memberRepository.save(member);

        MemberDeleteServiceRequest request = MemberDeleteServiceRequest.builder()
                .deleteMemberId(member.getMemberId())
                .password(password)
                .build();

        //when
        memberService.deleteMember(member.getMemberId(), request);

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
        Member member = createMember();
        Member otherMember = createMember(encodedPassword);

        memberRepository.save(member);
        memberRepository.save(otherMember);

        MemberDeleteServiceRequest request = MemberDeleteServiceRequest.builder()
                .deleteMemberId(otherMember.getMemberId()) // 다른 멤버를 삭제
                .password(password)
                .build();

        //when
        assertThatThrownBy(() -> memberService.deleteMember(member.getMemberId(), request))
                .isInstanceOf(MemberAccessDeniedException.class)
                .hasMessageContaining("접근 권한이 없습니다.");
    }

    @Test
    @DisplayName("memberId 를 통해 해당 멤버의 정보, 멤버의 게시물, 댓글, 태그 정보를 모두 가져온다.")
    void getMember() {
        //given
        Member member = createMember();
        Member otherMember = createMember();

        memberRepository.save(member);
        memberRepository.save(otherMember);

        Tag tag1 = createTag("tag1");
        Tag tag2 = createTag("tag2");

        em.persist(tag1);
        em.persist(tag2);

        List<Question> questions = createQuestions(member, 10);

        questions.forEach(question -> {
            em.persist(question);
            em.persist(createQuestionTag(question, tag1));
            em.persist(createQuestionTag(question, tag2));
        });

        List<Question> otherQuestions = createQuestions(otherMember, 10);

        otherQuestions.forEach(otherQuestion -> {
            em.persist(otherQuestion);
            em.persist(createAnswer(member, otherQuestion));
        });

        //when
        MemberResponse response = memberService.getMember(member.getMemberId());

        //then
        assertThat(response.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(response.getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getNickname()).isEqualTo(member.getNickname());
        assertThat(response.getMyIntro()).isEqualTo(member.getMyInfo().getMyIntro());
        assertThat(response.getQuestion().getQuestions()).hasSize(5);
        assertThat(response.getQuestion().getPageInfo().getPage()).isEqualTo(1);
        assertThat(response.getQuestion().getPageInfo().getSize()).isEqualTo(5);
        assertThat(response.getQuestion().getPageInfo().getTotalSize()).isEqualTo(10);
        assertThat(response.getAnswer().getAnswers()).hasSize(5);
        assertThat(response.getAnswer().getPageInfo().getPage()).isEqualTo(1);
        assertThat(response.getAnswer().getPageInfo().getSize()).isEqualTo(5);
        assertThat(response.getAnswer().getPageInfo().getTotalSize()).isEqualTo(10);
        assertThat(response.getTags()).hasSize(2);
    }

    @Test
    @DisplayName("memberId 와 페이지를 통해 해당 멤버의 최신 순 게시물을 가져온다.")
    void getMemberQuestion() {
        //given
        Member member = createMember();

        memberRepository.save(member);

        List<Question> questions = createQuestions(member, 10);

        questions.forEach(em::persist);

        //when
        MemberResponse.MemberQuestionPageResponse memberQuestion =
                memberService.getMemberQuestion(member.getMemberId(), 0, 5);

        //then
        assertThat(memberQuestion.getQuestions()).hasSize(5);
        assertThat(memberQuestion.getPageInfo().getPage()).isEqualTo(1);
        assertThat(memberQuestion.getPageInfo().getSize()).isEqualTo(5);
        assertThat(memberQuestion.getPageInfo().getTotalSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("memberId 와 페이지를 통해 해당 멤버의 최신 순 답변 목록을 가져온다.")
    void getMemberAnswer() {
        //given
        Member member = createMember();
        Member otherMember = createMember();

        memberRepository.save(member);
        memberRepository.save(otherMember);

        List<Question> otherQuestions = createQuestions(otherMember, 10);

        otherQuestions.forEach(otherQuestion -> {
            em.persist(otherQuestion);
            em.persist(createAnswer(member, otherQuestion));
        });

        //when
        MemberResponse.MemberAnswerPageResponse memberAnswer =
                memberService.getMemberAnswer(member.getMemberId(), 0, 5);

        //then
        assertThat(memberAnswer.getAnswers()).hasSize(5);
        assertThat(memberAnswer.getPageInfo().getPage()).isEqualTo(1);
        assertThat(memberAnswer.getPageInfo().getSize()).isEqualTo(5);
        assertThat(memberAnswer.getPageInfo().getTotalSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("s3 버킷에 image 를 업로드하고 업로드한 이미지의 presigned url 을 반환한다.")
    void updateImage() {
        //given
        Member member = createMember();

        memberRepository.save(member);

        Long loginMemberId = member.getMemberId();
        Long updateMemberId = member.getMemberId();
        MultipartFile image =
                new MockMultipartFile(
                        "image",
                        "image.png",
                        "image/png",
                        "image".getBytes());

        given(s3Service.uploadImage(anyString(), any(MultipartFile.class)))
                .willReturn("https://image.png");

        //when
        String imageUrl = memberService.updateImage(loginMemberId, updateMemberId, image);

        //then
        assertThat(imageUrl).isEqualTo("https://image.png");
    }

    @Test
    @DisplayName("s3 버킷에서 해당 멤버의 이미지를 삭제하고 Myinfo 의 image 를 null 로 만든다.")
    void deleteImage() {
        //given
        Member member = createMember();

        memberRepository.save(member);

        Long loginMemberId = member.getMemberId();
        Long updateMemberId = member.getMemberId();

        willDoNothing().given(s3Service).deleteImage(anyString());

        //when
        memberService.deleteImage(loginMemberId, updateMemberId);

        //then
        assertThat(member.getMyInfo().getImage()).isNull();
    }

    @Test
    @DisplayName("이메일 인증을 시도하면 이메일로 인증 코드를 보내고 redis 에 저장한다.")
    void sendCodeToEmail() {
        //given
        String email = "test@google.com";

        given(emailSender.createMimeMessage())
                .willReturn(new MimeMessage(Session.getInstance(new Properties())));

        //when
        memberService.sendCodeToEmail(email);

        //then
        verify(redisService, times(1)).saveValues(anyString(), anyString(), any(Duration.class)); //저장 로직 호출
        verify(emailSender, times(1)).send(any(MimeMessage.class)); //이메일 전송 로직 호출

    }

    @Test
    @DisplayName("이메일 인증 시도 시 중복된 이메일이면 MemberDuplicateException 이 발생한다.")
    void sendCodeToEmailException() {
        //given
        Member member = createMember();
        memberRepository.save(member);

        String email = member.getEmail();

        //when & then
        assertThatThrownBy(() -> memberService.sendCodeToEmail(email))
                .isInstanceOf(MemberDuplicateException.class)
                .hasMessageContaining("이미 존재하는 회원 이메일입니다.");
    }

    @Test
    @DisplayName("이메일로 온 코드를 인증할 때 redis 에 저장된 코드와 일치하면 true 를 반환한다.")
    void checkCode() {
        //given
        String email = "test@google.com";
        String code = "123456";

        given(redisService.getValues(anyString()))
                .willReturn(code);

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

        given(redisService.getValues(anyString()))
                .willReturn("654321");

        //when
        boolean result = memberService.checkCode(email, code);

        //then
        assertThat(result).isFalse();
    }

    private Question createQuestion(Member member) {
        return Question.builder()
                .title("title")
                .content("content")
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

    private Tag createTag(String name){
        return Tag.builder()
                .tagName(name)
                .build();
    }

    private QuestionTag createQuestionTag(Question question, Tag tag){
        return QuestionTag.builder()
                .question(question)
                .tag(tag)
                .build();
    }
}