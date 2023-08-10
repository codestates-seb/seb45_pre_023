package sixman.stackoverflow.domain.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.member.service.dto.request.MemberCreateServiceRequest;

@AllArgsConstructor
@Getter
@Builder
public class MemberCreateApiRequest {

    private String email;
    private String nickname;
    private String password;

    public MemberCreateServiceRequest toResponseRequest() {
        return MemberCreateServiceRequest.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();
    }
}
