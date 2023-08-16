package sixman.stackoverflow.auth.oauth.service;

import lombok.Getter;
import lombok.Setter;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;

@Getter @Setter
public class MemberProfile {

    private String email;

    public Member toMember() {
        return Member.builder()
                .email(email)
                .nickname(email.split("@")[0])
                .password("oauthUser")
                .authority(Authority.ROLE_USER)

                .build();
    }
}
