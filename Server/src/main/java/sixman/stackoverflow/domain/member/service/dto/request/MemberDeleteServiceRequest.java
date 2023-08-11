package sixman.stackoverflow.domain.member.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDeleteServiceRequest {

    private Long deleteMemberId;
    private String password;
}
