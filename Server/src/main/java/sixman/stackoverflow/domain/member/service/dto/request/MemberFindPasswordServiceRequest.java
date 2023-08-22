package sixman.stackoverflow.domain.member.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class MemberFindPasswordServiceRequest {

    private String email;
    private String password;
}
