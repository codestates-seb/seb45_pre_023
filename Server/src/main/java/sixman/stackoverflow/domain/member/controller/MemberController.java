package sixman.stackoverflow.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.domain.member.controller.dto.MemberCreateApiRequest;
import sixman.stackoverflow.domain.member.service.MemberService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid MemberCreateApiRequest request) {

        Long memberId = memberService.signup(request.toResponseRequest());

        URI uri = URI.create("/members/" + memberId);

        return ResponseEntity.created(uri).build();
    }
}
