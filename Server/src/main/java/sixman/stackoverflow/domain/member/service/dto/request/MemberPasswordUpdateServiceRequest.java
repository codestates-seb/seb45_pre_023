package sixman.stackoverflow.domain.member.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class MemberPasswordUpdateServiceRequest {

    private Long updateMemberId;
    private String password;
    private String newPassword;
}
