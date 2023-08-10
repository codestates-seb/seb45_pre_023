package sixman.stackoverflow.domain.member.controller;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sixman.stackoverflow.domain.member.controller.dto.MemberDeleteApiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberPasswordUpdateAPiRequest;
import sixman.stackoverflow.domain.member.controller.dto.MemberUpdateApiRequest;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.service.MemberService;
import sixman.stackoverflow.domain.member.service.dto.response.MemberResponse;
import sixman.stackoverflow.global.response.ApiSingleResponse;
import sixman.stackoverflow.global.response.PageInfo;
import sixman.stackoverflow.module.aws.s3service.S3Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final S3Service s3Service;

    public MemberController(MemberService memberService, S3Service s3Service) {
        this.memberService = memberService;
        this.s3Service = s3Service;
    }

    @GetMapping("/{member-id}")
    public ResponseEntity<ApiSingleResponse<MemberResponse>> getMember(@PathVariable("member-id") Long memberId) {

        MemberResponse memberResponse = getMemberResponse(memberId);

        return ResponseEntity.ok(ApiSingleResponse.ok(memberResponse));
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity<Void> updateMember(@PathVariable("member-id") Long memberId,
                                             @RequestBody @Valid MemberUpdateApiRequest request) {

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{member-id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable("member-id") Long memberId,
                                             @RequestBody @Valid MemberPasswordUpdateAPiRequest request) {

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{member-id}/image")
    public ResponseEntity<Void> updateImage(@PathVariable("member-id") Long memberId,
                                            @RequestParam MultipartFile file
                                             ) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "bucket url");

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("member-id") Long memberId,
                                             @RequestBody @Valid MemberDeleteApiRequest request) {

        return ResponseEntity.noContent().build();
    }


    private MemberResponse getMemberResponse(Long memberId) {
        List<MemberResponse.MemberQuestion> questions = new ArrayList<>();
        List<MemberResponse.MemberAnswer> answers = new ArrayList<>();

        for(long i = 1; i <= 5; i++){

            MemberResponse.MemberQuestion question = MemberResponse.MemberQuestion.builder()
                    .questionId(i)
                    .title("title " + i)
                    .views(100)
                    .recommend(10)
                    .createdDate(LocalDateTime.now().minusDays(1))
                    .updatedDate(LocalDateTime.now())
                    .build();

            questions.add(question);

            MemberResponse.MemberAnswer answer = MemberResponse.MemberAnswer.builder()
                    .answerId(i)
                    .questionTitle("title " + i)
                    .questionId(i)
                    .content("content " + i)
                    .recommend(10)
                    .createdDate(LocalDateTime.now().minusDays(1))
                    .updatedDate(LocalDateTime.now())
                    .build();

            answers.add(answer);
        }

        MemberResponse.MemberQuestionPageResponse question = MemberResponse.MemberQuestionPageResponse.builder()
                .questions(questions)
                .pageInfo(PageInfo.of(new PageImpl(questions, PageRequest.of(1, 10), 100)))
                .build();

        MemberResponse.MemberAnswerPageResponse answer = MemberResponse.MemberAnswerPageResponse.builder()
                .answers(answers)
                .pageInfo(PageInfo.of(new PageImpl(answers, PageRequest.of(1, 10), 100)))
                .build();

        List<MemberResponse.MemberTag> tags = new ArrayList<>();

        for(long i = 1; i <= 5; i++){
            MemberResponse.MemberTag tag = MemberResponse.MemberTag.builder()
                    .tagId(i)
                    .tagName("tag " + i)
                    .build();

            tags.add(tag);
        }

        MemberResponse memberResponse = MemberResponse.builder()
                .memberId(memberId)
                .email("test@test.com")
                .nickname("nickname")
                .image(s3Service.getPreSignedUrl("https://sixman-images-test.s3.ap-northeast-2.amazonaws.com/test.png"))
                .myIntro("hi! im test")
                .authority(Authority.ROLE_USER)
                .question(question)
                .answer(answer)
                .tags(tags)
                .build();

        return memberResponse;
    }


}
