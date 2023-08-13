package sixman.stackoverflow.domain.reply.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sixman.stackoverflow.domain.reply.entity.Reply;
@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
