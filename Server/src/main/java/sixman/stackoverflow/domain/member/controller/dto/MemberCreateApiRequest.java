package sixman.stackoverflow.domain.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.member.service.dto.request.MemberCreateServiceRequest;

import javax.validation.constraints.*;

@AllArgsConstructor
@Getter
@Builder
public class MemberCreateApiRequest {

    @Email(message = "{validation.member.email}")
    @NotBlank(message = "{validation.member.email}")
    private String email;
    @Size(min = 1, max = 15, message = "{validation.size}")
    @NotBlank(message = "{validation.member.nickname}")
    private String nickname;
    @NotNull(message = "{validation.member.password}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]*$", message = "{validation.member.password}")
    @Size(min = 9, max = 20, message = "{validation.size}")
    private String password;

    public MemberCreateServiceRequest toServiceRequest() {
        return MemberCreateServiceRequest.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();
    }
}
