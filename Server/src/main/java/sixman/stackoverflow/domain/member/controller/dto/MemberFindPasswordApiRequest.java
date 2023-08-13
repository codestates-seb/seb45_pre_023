package sixman.stackoverflow.domain.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sixman.stackoverflow.domain.member.service.dto.request.MemberFindPasswordServiceRequest;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberFindPasswordApiRequest {

    @Email(message = "{validation.member.email}")
    @NotNull(message = "{validation.member.email}")
    private String email;
    @NotNull(message = "{validation.member.password}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]*$", message = "{validation.member.password}")
    @Size(min = 9, max = 20, message = "{validation.size}")
    private String password;

    public MemberFindPasswordServiceRequest toServiceRequest() {
        return MemberFindPasswordServiceRequest.builder()
                .email(email)
                .password(password)
                .build();
    }
}
