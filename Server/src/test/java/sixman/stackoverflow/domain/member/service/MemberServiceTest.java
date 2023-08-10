package sixman.stackoverflow.domain.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;
import sixman.stackoverflow.domain.member.service.dto.request.MemberCreateServiceRequest;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberDuplicateException;
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
}