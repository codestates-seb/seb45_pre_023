package sixman.stackoverflow.domain.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/members")
@Validated
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{member-id}")
    public ResponseEntity<ApiSingleResponse<MemberResponse>> getMember(@PathVariable("member-id") Long memberId) {

        MemberResponse response = memberService.getMember(memberId);

        return ResponseEntity.ok(ApiSingleResponse.ok(response));
    }

    @GetMapping("/{member-id}/questions")
    public ResponseEntity<ApiPageResponse<MemberResponse.MemberQuestion>> getMemberQuestion(
            @PathVariable("member-id") Long memberId,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {

        MemberResponse.MemberQuestionPageResponse memberQuestion
                = memberService.getMemberQuestion(memberId, page - 1, 5);

        Page<MemberResponse.MemberQuestion> memberQuestionPage = new PageImpl<>(
                memberQuestion.getQuestions(),
                PageRequest.of(page - 1, 5),
                memberQuestion.getPageInfo().getTotalSize());

        return ResponseEntity.ok(ApiPageResponse.ok(memberQuestionPage));
    }

    @GetMapping("/{member-id}/answers")
    public ResponseEntity<ApiPageResponse<MemberResponse.MemberAnswer>> getMemberAnswer(
            @PathVariable("member-id") Long memberId,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {

        MemberResponse.MemberAnswerPageResponse memberAnswerPageResponse
                = memberService.getMemberAnswer(memberId, page - 1, 5);

        Page<MemberResponse.MemberAnswer> memberAnswerPage
                = new PageImpl<>(
                memberAnswerPageResponse.getAnswers(),
                PageRequest.of(page - 1, 5),
                memberAnswerPageResponse.getPageInfo().getTotalSize());

        return ResponseEntity.ok(ApiPageResponse.ok(memberAnswerPage));
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity<Void> updateMember(@PathVariable("member-id") Long updateMemberId,
                                             @RequestBody @Valid MemberUpdateApiRequest request) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        memberService.updateMember(loginMemberId, request.toServiceRequest(updateMemberId));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{member-id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable("member-id") Long updateMemberId,
                                             @RequestBody @Valid MemberPasswordUpdateAPiRequest request) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        memberService.updatePassword(loginMemberId, request.toServiceRequest(updateMemberId));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{member-id}/image")
    public ResponseEntity<Void> updateImage(@PathVariable("member-id") Long updateMemberId,
                                            @RequestParam MultipartFile file
                                             ) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        String presignedUrl = memberService.updateImage(loginMemberId, updateMemberId, file);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", presignedUrl);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{member-id}/image")
    public ResponseEntity<Void> deleteImage(@PathVariable("member-id") Long updateMemberId) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        memberService.deleteImage(loginMemberId, updateMemberId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("member-id") Long deleteMemberId,
                                             @RequestBody @Valid MemberDeleteApiRequest request) {

        Long loginMemberId = SecurityUtil.getCurrentId();

        memberService.deleteMember(loginMemberId, request.toServiceRequest(deleteMemberId));

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@RequestBody @Valid MemberMailAuthApiRequest request) {
        memberService.sendCodeToEmail(request.getEmail());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email/confirm")
    public ResponseEntity<ApiSingleResponse<Boolean>> confirmEmail(@RequestBody @Valid MemberMailConfirmApiRequest request) {
        boolean result = memberService.checkCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(ApiSingleResponse.ok(result));
    }
}
