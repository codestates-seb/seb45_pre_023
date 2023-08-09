package sixman.stackoverflow.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class MemberController {

    @GetMapping("/auth")
    public String index(){
        return "로그인된 사용자만 접근 가능한 페이지";
    }

    @GetMapping("/userInfo")
    public String oauthLoginInfo(Authentication authentication){

        if(authentication.getPrincipal() instanceof OAuth2User){
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            return attributes.toString();
        }

        return "Session login";
    }
}
