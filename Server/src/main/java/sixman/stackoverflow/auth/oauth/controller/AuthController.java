package sixman.stackoverflow.auth.oauth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sixman.stackoverflow.auth.jwt.dto.Token;
import sixman.stackoverflow.auth.oauth.service.OAuthService;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final OAuthService oAuthService;

    public AuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/oauth/{provider}")
    public ResponseEntity<Void> login(@PathVariable String provider, String code) {
        Token token = oAuthService.login(provider, code);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("Authorization", Collections.singletonList("Bearer " + token.getAccessToken()));
        map.put("Refresh", Collections.singletonList("Bearer " + token.getRefreshToken()));
        HttpHeaders tokenHeader = new HttpHeaders(map);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(tokenHeader).body(null);
    }
}
