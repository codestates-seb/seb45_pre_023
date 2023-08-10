package sixman.stackoverflow.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sixman.stackoverflow.domain.member.controller.dto.MemberCreateApiRequest;
import sixman.stackoverflow.domain.member.service.MemberService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


}
