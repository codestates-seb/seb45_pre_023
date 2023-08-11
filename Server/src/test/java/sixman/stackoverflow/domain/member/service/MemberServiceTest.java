package sixman.stackoverflow.domain.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.member.service.dto.request.MemberCreateServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.request.MemberDeleteServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.request.MemberPasswordUpdateServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.request.MemberUpdateServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.domain.question.entity.Question;
import sixman.stackoverflow.domain.questiontag.entity.QuestionTag;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberAccessDeniedException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberDuplicateException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberPasswordException;
import sixman.stackoverflow.global.testhelper.ServiceTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberServiceTest extends ServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

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