package sixman.stackoverflow.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.auth.jwt.dto.Token;
import sixman.stackoverflow.auth.jwt.service.TokenProvider;
import sixman.stackoverflow.auth.oauth.service.Provider;
import sixman.stackoverflow.auth.oauth.service.OAuthService;
import sixman.stackoverflow.domain.member.controller.dto.MemberCreateApiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberFindPasswordApiRequest;
import sixman.stackoverflow.domain.member.service.MemberService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final OAuthService oAuthService;
    private final MemberService memberService;

    public AuthController(OAuthService oAuthService, MemberService memberService) {
        this.oAuthService = oAuthService;
        this.memberService = memberService;
    }

    @GetMapping("/oauth")
    public ResponseEntity<String> login(Provider provider, String code) {
        
        Token token = oAuthService.login(provider, code);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("Authorization", Collections.singletonList("Bearer " + token.getAccessToken()));
        map.put("Refresh", Collections.singletonList("Bearer " + token.getRefreshToken()));
        HttpHeaders tokenHeader = new HttpHeaders(map);

        String jsonResponse = "{\"memberId\":" + token.getMemberId() + "}";

        return ResponseEntity.ok().headers(tokenHeader).body(jsonResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid MemberCreateApiRequest request) {

        Long memberId = memberService.signup(request.toServiceRequest());

        URI uri = URI.create("/members/" + memberId);

        return ResponseEntity.created(uri).build();
    }


    @PatchMapping("/password")
    public ResponseEntity<Void> findPassword(@RequestBody @Valid MemberFindPasswordApiRequest request) {

        memberService.findPassword(request.toServiceRequest());

        return ResponseEntity.noContent().build();
    }

}
