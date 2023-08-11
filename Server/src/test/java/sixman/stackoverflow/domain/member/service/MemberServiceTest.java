package sixman.stackoverflow.domain.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.member.service.dto.request.MemberCreateServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.request.MemberPasswordUpdateServiceRequest;
import sixman.stackoverflow.domain.member.service.dto.request.MemberUpdateServiceRequest;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberDuplicateException;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberPasswordException;
import sixman.stackoverflow.global.testhelper.ServiceTest;

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
    @DisplayName("이전 비밀번호와 새 비밀번호를 받아서 비밀번호를 수정한다.")
    void updatePassword() {
        //given
        String password = "1234abcd!";
        String newPassword = "1234abcd!!";
        String encodedPassword = passwordEncoder.encode(password);
        Member member = createMember(encodedPassword);

        memberRepository.save(member);

        MemberPasswordUpdateServiceRequest request = MemberPasswordUpdateServiceRequest.builder()
                .password(password)
                .newPassword(newPassword)
                .build();

        //when
        memberService.updatePassword(member.getMemberId(), request);

        //then
        assertThat(passwordEncoder.matches(newPassword, member.getPassword())).isTrue();
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
                .password(password + "wrong password") // 이전 비밀번호가 틀림
                .newPassword(newPassword)
                .build();

        //when & then
        assertThatThrownBy(() -> memberService.updatePassword(member.getMemberId(), request))
                .isInstanceOf(MemberPasswordException.class)
                .hasMessageContaining("비밀번호를 확인해주세요.");
    }
}