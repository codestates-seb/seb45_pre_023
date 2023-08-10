package sixman.stackoverflow.domain.member.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.global.response.PageInfo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {

    private Long memberId;
    private String email;
    private String nickname;
    private String image;
    private String myIntro;
    private Authority authority;
    private MemberQuestionPageResponse question;
    private MemberAnswerPageResponse answer;
    private List<MemberTag> tags;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberQuestionPageResponse {
        private List<MemberQuestion> questions;
        private PageInfo pageInfo;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberQuestion {
        private Long questionId;
        private String title;
        private Integer views;
        private Integer recommend;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberAnswerPageResponse {
        private List<MemberAnswer> answers;
        private PageInfo pageInfo;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberAnswer {
        private Long answerId;
        private Long questionId;
        private String questionTitle;
        private String content;
        private Integer recommend;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberTag {
        private Long tagId;
        private String tagName;
    }

}
