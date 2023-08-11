package sixman.stackoverflow.domain.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sixman.stackoverflow.domain.member.repository.dto.MemberAnswerData;
import sixman.stackoverflow.domain.member.repository.dto.MemberQuestionData;
import sixman.stackoverflow.domain.tag.entity.Tag;

import java.util.List;

public interface MemberRepositoryCustom {

    Page<MemberQuestionData> findQuestionByMemberId(Long memberId, Pageable pageable);

    Page<MemberAnswerData> findAnswerByMemberId(Long memberId, Pageable pageable);

    List<Tag> findTagByMemberId(Long memberId);
}
