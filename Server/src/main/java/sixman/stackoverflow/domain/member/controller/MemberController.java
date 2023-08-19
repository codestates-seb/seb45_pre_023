package sixman.stackoverflow.domain.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sixman.stackoverflow.auth.utils.SecurityUtil;
import sixman.stackoverflow.domain.member.controller.dto.*;
import sixman.stackoverflow.domain.member.service.MemberService;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.global.response.ApiPageResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.http.HttpHeaders.*;

@RestController
@RequestMapping("/members")
@Validated
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{member-id}")
    public ResponseEntity<ApiSingleResponse<MemberResponse>> getMember(
            @PathVariable("member-id") @Positive Long memberId) {

        MemberResponse response = memberService.getMember(memberId);

        return ResponseEntity.ok(ApiSingleResponse.ok(response));
    }

    @GetMapping("/{member-id}/questions")
    public ResponseEntity<ApiPageResponse<MemberResponse.MemberQuestion>> getMemberQuestion(
            @PathVariable("member-id") @Positive Long memberId,
            @RequestParam(value = "page", defaultValue = "1") @Positive int page
    ) {

        Page<MemberResponse.MemberQuestion> memberQuestionPage
                = memberService.getMemberQuestion(memberId, page - 1, 5);

        return ResponseEntity.ok(ApiPageResponse.ok(memberQuestionPage));
    }

    @GetMapping("/{member-id}/answers")
    public ResponseEntity<ApiPageResponse<MemberResponse.MemberAnswer>> getMemberAnswer(
            @PathVariable("member-id") @Positive Long memberId,
            @RequestParam(value = "page", defaultValue = "1") @Positive int page
    ) {

        Page<MemberResponse.MemberAnswer> memberAnswerPage
                = memberService.getMemberAnswer(memberId, page - 1, 5);

        return ResponseEntity.ok(ApiPageResponse.ok(memberAnswerPage));
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity<Void> updateMember(@PathVariable("member-id") @Positive Long updateMemberId,
                                             @RequestBody @Valid MemberUpdateApiRequest request) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        memberService.updateMember(loginMemberId, request.toServiceRequest(updateMemberId));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{member-id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable("member-id") @Positive Long updateMemberId,
                                             @RequestBody @Valid MemberPasswordUpdateAPiRequest request) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        memberService.updatePassword(loginMemberId, request.toServiceRequest(updateMemberId));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{member-id}/image")
    public ResponseEntity<Void> updateImage(@PathVariable("member-id") @Positive Long updateMemberId,
                                            @RequestParam MultipartFile file) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        String presignedUrl = memberService.updateImage(loginMemberId, updateMemberId, file);

        return ResponseEntity.noContent().header(LOCATION, presignedUrl).build();
    }

    @DeleteMapping("/{member-id}/image")
    public ResponseEntity<Void> deleteImage(@PathVariable("member-id") @Positive Long updateMemberId) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        memberService.deleteImage(loginMemberId, updateMemberId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("member-id") @Positive Long deleteMemberId) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        memberService.deleteMember(loginMemberId, deleteMemberId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@RequestBody @Valid MemberMailAuthApiRequest request) {

        memberService.sendSignupCodeToEmail(request.getEmail());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email/confirm")
    public ResponseEntity<ApiSingleResponse<Boolean>> confirmEmail(
            @RequestBody @Valid MemberMailConfirmApiRequest request) {

        boolean result = memberService.checkCode(request.getEmail(), request.getCode());

        return ResponseEntity.ok(ApiSingleResponse.ok(result));
    }
}
