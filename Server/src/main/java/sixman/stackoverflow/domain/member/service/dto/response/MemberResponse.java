package sixman.stackoverflow.domain.member.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.dto.MemberAnswerData;
import sixman.stackoverflow.domain.member.repository.dto.MemberQuestionData;
import sixman.stackoverflow.domain.tag.entity.Tag;
import sixman.stackoverflow.global.response.PageInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {

    private Long memberId;
    private String email;
    private String nickname;
    private String image;
    private String myIntro;
    private String title;
    private String location;
    private List<String> accounts;
    private Authority authority;
    private MemberQuestionPageResponse question;
    private MemberAnswerPageResponse answer;
    private List<MemberTag> tags;

    public static MemberResponse of(Member member,
                                    String imageUrl,
                                    Page<MemberQuestion> question,
                                    Page<MemberAnswer> answer,
                                    List<MemberTag> tags) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .image(imageUrl)
                .myIntro(member.getMyInfo().getMyIntro())
                .title(member.getMyInfo().getTitle())
                .location(member.getMyInfo().getLocation())
                .accounts(member.getMyInfo().getAccounts())
                .authority(member.getAuthority())
                .question(MemberQuestionPageResponse.of(question))
                .answer(MemberAnswerPageResponse.of(answer))
                .tags(tags)
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberQuestionPageResponse {
        private List<MemberQuestion> questions;
        private PageInfo pageInfo;

        public static MemberQuestionPageResponse of(Page<MemberQuestion> data) {
            return MemberQuestionPageResponse.builder()
                    .questions(data.getContent())
                    .pageInfo(PageInfo.of(data))
                    .build();
        }
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

        public static MemberQuestion of(MemberQuestionData data) {
            return MemberQuestion.builder()
                    .questionId(data.getQuestionId())
                    .title(data.getTitle())
                    .views(data.getViews())
                    .recommend(data.getRecommend())
                    .createdDate(data.getCreatedDate())
                    .updatedDate(data.getUpdatedDate())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberAnswerPageResponse {
        private List<MemberAnswer> answers;
        private PageInfo pageInfo;

        public static MemberAnswerPageResponse of(Page<MemberAnswer> data) {
            return MemberAnswerPageResponse.builder()
                    .answers(data.getContent())
                    .pageInfo(PageInfo.of(data))
                    .build();
        }
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

        public static MemberAnswer of(MemberAnswerData data) {
            return MemberAnswer.builder()
                    .answerId(data.getAnswerId())
                    .questionId(data.getQuestionId())
                    .questionTitle(data.getQuestionTitle())
                    .content(data.getContent())
                    .recommend(data.getRecommend())
                    .createdDate(data.getCreatedDate())
                    .updatedDate(data.getUpdatedDate())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemberTag {
        private Long tagId;
        private String tagName;

        public static List<MemberTag> of(List<Tag> tags) {
            return tags.stream()
                    .map(MemberTag::of)
                    .collect(Collectors.toList());
        }

        public static MemberTag of(Tag tag) {
            return MemberTag.builder()
                    .tagId(tag.getTagId())
                    .tagName(tag.getTagName())
                    .build();
        }
    }

}
