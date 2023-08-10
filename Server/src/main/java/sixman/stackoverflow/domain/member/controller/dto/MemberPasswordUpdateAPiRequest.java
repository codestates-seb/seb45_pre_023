package sixman.stackoverflow.domain.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class MemberPasswordUpdateAPiRequest {

    private String password;
    private String newPassword;
}
