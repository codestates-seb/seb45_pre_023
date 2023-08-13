package sixman.stackoverflow.domain.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberMailConfirmApiRequest {

    @Email(message = "{validation.member.email}")
    @NotNull(message = "{validation.member.email}")
    private String email;
    @NotBlank(message = "{validation.member.code}")
    private String code;
}
