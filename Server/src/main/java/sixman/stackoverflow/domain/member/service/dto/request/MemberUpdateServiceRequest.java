package sixman.stackoverflow.domain.member.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class MemberUpdateServiceRequest {

    private Long updateMemberId;
    private String nickname;
    private String myIntro;
    private String title;
    private String location;
    private List<String> accounts;
}
