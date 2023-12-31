package sixman.stackoverflow.auth.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Token {

    private String accessToken;
    private String refreshToken;
    private Long memberId;
}
