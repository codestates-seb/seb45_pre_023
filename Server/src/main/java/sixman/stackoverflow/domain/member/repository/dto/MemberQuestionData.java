package sixman.stackoverflow.domain.member.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class MemberQuestionData {

    private Long questionId;
    private String title;
    private Integer views;
    private Integer recommend;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @QueryProjection
    public MemberQuestionData(Long questionId, String title, Integer views, Long votes, Long downs, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.questionId = questionId;
        this.title = title;
        this.views = views;
        this.recommend = votes.intValue() - downs.intValue() * 2;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

}
