package sixman.stackoverflow.domain.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.member.service.dto.request.MemberUpdateServiceRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class MemberUpdateApiRequest {

    @Size(min = 1, max = 15, message = "{validation.size}")
    private String nickname;
    private String myIntro;
    private String title;
    private String location;
    private List<String> accounts;

    public MemberUpdateServiceRequest toServiceRequest(Long updateMemberId) {
        return MemberUpdateServiceRequest.builder()
                .updateMemberId(updateMemberId)
                .nickname(nickname)
                .myIntro(myIntro)
                .title(title)
                .location(location)
                .accounts(accounts)
                .build();
    }
}
