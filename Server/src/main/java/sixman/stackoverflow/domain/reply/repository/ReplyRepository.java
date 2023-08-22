package sixman.stackoverflow.domain.reply.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sixman.stackoverflow.domain.answer.entitiy.Answer;
import sixman.stackoverflow.domain.reply.entity.Reply;

import java.util.List;


public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Page<Reply> findByAnswer(Answer answer, Pageable pageable);
}
