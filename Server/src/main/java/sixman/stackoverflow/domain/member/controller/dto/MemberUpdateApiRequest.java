package sixman.stackoverflow.domain.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.member.service.dto.request.MemberUpdateServiceRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Builder
public class MemberUpdateApiRequest {

    @Size(min = 1, max = 15, message = "{validation.size}")
    @NotBlank(message = "{validation.member.nickname}")
    private String nickname;
    private String myIntro;

    public MemberUpdateServiceRequest toServiceRequest(Long updateMemberId) {
        return MemberUpdateServiceRequest.builder()
                .updateMemberId(updateMemberId)
                .nickname(nickname)
                .myIntro(myIntro)
                .build();
    }
}
