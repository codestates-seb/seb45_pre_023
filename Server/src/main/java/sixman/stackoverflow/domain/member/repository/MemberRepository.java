package sixman.stackoverflow.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixman.stackoverflow.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long memberId);
}