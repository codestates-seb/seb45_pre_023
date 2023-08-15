package sixman.stackoverflow.domain.member.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sixman.stackoverflow.domain.member.entity.Member;

@Getter
@Builder
@AllArgsConstructor
public class MemberInfo{
    private Long memberId;
    private String nickname;
    private String imageUrl;

    public static MemberInfo of(Member member) {
        return MemberInfo.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .imageUrl(member.getMyInfo().getImage())
                .build();
    }
}
