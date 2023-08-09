package sixman.stackoverflow.domain.member.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}