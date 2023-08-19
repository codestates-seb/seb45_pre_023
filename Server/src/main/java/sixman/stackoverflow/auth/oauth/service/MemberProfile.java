package sixman.stackoverflow.auth.oauth.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;

@Getter
@Builder
@AllArgsConstructor
public class MemberProfile {

    private String email;
}
