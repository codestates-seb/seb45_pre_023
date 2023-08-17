package sixman.stackoverflow.domain.member.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sixman.stackoverflow.global.exception.businessexception.memberexception.MemberPasswordException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    @DisplayName("email, nickname, password 를 받아 Member 객체를 생성한다.")
    void createMember() {
        // given
        String email = "test@test.com";
        String nickname = "test";
        String password = "1234abcd!";

        // when
        Member member = Member.createMember(email, nickname, password);

        // then
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getNickname()).isEqualTo(nickname);
        assertThat(member.getPassword()).isEqualTo(password);
        assertThat(member.getAuthority()).isEqualTo(Authority.ROLE_USER);
        assertThat(member.getMyInfo()).isNotNull();
    }
    
    @Test
    @DisplayName("nickname 를 받아서 Member 객체를 수정한다.")
    void updateMember() {
        //given
        Member member = createMemberDefault();
    
        //when
        member.updateMember("new nickname");

        //then
        assertThat(member.getNickname()).isEqualTo("new nickname");
    }

    @Test
    @DisplayName("member 객체 수정 시 nickname 이 null 이면 수정하지 않는다.")
    void updateMemberNickname() {
        //given
        Member member = createMemberDefault();

        //when
        member.updateMember(null);

        //then
        assertThat(member.getNickname()).isEqualTo("test");
    }

    @Test
    @DisplayName("member 의 myInfo 정보(title, location, accounts, myIntro) 를 수정할 수 있다.")
    void updateMyInfo() {
        //given
        Member member = createMemberDefault();
        String myIntro = "new myIntro";
        String title = "new title";
        String location = "new location";
        List<String> accounts = List.of("account1", "account2");

        //when
        member.updateMyInfo(myIntro, title, location, accounts);

        //then
        assertThat(member.getMyInfo().getMyIntro()).isEqualTo(myIntro);
        assertThat(member.getMyInfo().getTitle()).isEqualTo(title);
        assertThat(member.getMyInfo().getLocation()).isEqualTo(location);
        assertThat(member.getMyInfo().getAccounts()).isEqualTo(accounts);
    }

    @Test
    @DisplayName("member 의 새 비밀번호로 비밀번호를 수정할 수 있다.")
    void updatePassword() {
        //given
        Member member = createMemberDefault();
        String newPassword = "newPassword!@#";

        //when
        member.updatePassword(newPassword);

        //then
        assertThat(member.getPassword()).isEqualTo(newPassword);
    }

    private Member createMemberDefault() {
        return Member.builder()
                .email("test@test.com")
                .nickname("test")
                .password("1234abcd!")
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().build())
                .enabled(true)
                .build();
    }
}