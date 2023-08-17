package sixman.stackoverflow.domain.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.member.service.dto.request.MemberPasswordUpdateServiceRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Builder
public class MemberPasswordUpdateAPiRequest {

    @NotBlank(message = "{validation.member.password-notblank}")
    private String password;
    @NotNull(message = "{validation.member.password}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]*$", message = "{validation.member.password}")
    @Size(min = 9, max = 20, message = "{validation.size}")
    private String newPassword;

    public MemberPasswordUpdateServiceRequest toServiceRequest(Long updateMemberId) {
        return MemberPasswordUpdateServiceRequest.builder()
                .updateMemberId(updateMemberId)
                .password(password)
                .newPassword(newPassword)
                .build();
    }
}
