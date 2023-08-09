package todolist.domain.member.entity;

import lombok.RequiredArgsConstructor;
import todolist.global.entity.BaseEnum;

@RequiredArgsConstructor
public enum Authority implements BaseEnum {

    ROLE_USER("일반 사용자"),
    ROLE_ADMIN("관리자");

    private final String description;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
