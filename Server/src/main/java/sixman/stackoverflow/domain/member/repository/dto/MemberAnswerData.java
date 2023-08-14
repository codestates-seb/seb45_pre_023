package sixman.stackoverflow.domain.member.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class MemberAnswerData {
    private Long answerId;
    private Long questionId;
    private String questionTitle;
    private String content;
    private Integer recommend;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @QueryProjection
    public MemberAnswerData(Long answerId, Long questionId, String questionTitle, String content, Long votes, Long downs, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.content = content;
        this.recommend = votes.intValue() - downs.intValue() * 2;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
