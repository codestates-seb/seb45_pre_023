package sixman.stackoverflow.domain.member.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;
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
    public MemberQuestionData(Long questionId, String title, Integer views, Integer recommend, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.questionId = questionId;
        this.title = title;
        this.views = views;
        this.recommend = recommend;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

}
