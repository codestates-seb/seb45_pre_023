package sixman.stackoverflow.domain.reply.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class ReplyRepositoryTest {

    @Autowired ReplyRepository replyRepository;

    @Test
    void findByAnswer() {
    }
}