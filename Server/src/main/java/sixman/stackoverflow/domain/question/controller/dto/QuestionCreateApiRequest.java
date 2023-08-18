package sixman.stackoverflow.domain.question.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.question.entity.Question;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class QuestionCreateApiRequest {


    @NotBlank(message = "{validation.question.title}")
    private String title;

    @NotBlank(message = "{validation.question.content}")
    private String detail;

    @NotBlank(message = "{validation.question.content}")
    private String expect;

    private List<Integer> tagIds = new ArrayList<>();

    // 생성 메서드
    public Question toEntity(Member member) {
        return Question.builder()
                .title(title)
                .detail(detail)
                .expect(expect)
                .member(member)
                .build();
    }
}