package sixman.stackoverflow.domain.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberMailAuthApiRequest {

    @Email(message = "{validation.member.email}")
    @NotNull(message = "{validation.member.email}")
    private String email;
}
