package sixman.stackoverflow.domain.question.controller.dto;

import lombok.AllArgsConstructor;
import sixman.stackoverflow.global.entity.BaseEnum;

@AllArgsConstructor
public enum AnswerSortRequest implements BaseEnum {

    CREATED_DATE("최신순", "createdDate"),
    RECOMMEND("추천순", "recommend"),
    ;

    private String discription;

    private String value;

    @Override
    public String getName() {
        return name();
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String getDescription() {
        return this.discription;
    }
}
