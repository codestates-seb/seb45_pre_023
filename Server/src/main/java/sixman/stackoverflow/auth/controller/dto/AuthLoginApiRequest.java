package sixman.stackoverflow.auth.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.auth.oauth.service.Provider;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Builder
public class AuthLoginApiRequest {

    @NotNull(message = "{validation.auth.provider}")
    private Provider provider;
    private String code;
}
