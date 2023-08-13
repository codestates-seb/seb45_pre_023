package sixman.stackoverflow.global.testhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;
import sixman.stackoverflow.module.aws.service.S3Service;
import sixman.stackoverflow.module.email.service.MailService;
import sixman.stackoverflow.module.redis.service.RedisService;

import javax.persistence.EntityManager;

@Transactional
@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc
public abstract class ServiceTest {

    @MockBean protected S3Service s3Service;
    @MockBean protected RedisService redisService;
    @MockBean protected JavaMailSender emailSender;
    @Autowired protected PasswordEncoder passwordEncoder;
    @Autowired protected EntityManager em;

    protected Member createMember() {
        return Member.builder()
                .email("test@test.com")
                .nickname("test")
                .password(passwordEncoder.encode("1234abcd!"))
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().image("images/test.png").build())
                .enabled(true)
                .build();
    }

    protected Member createMember(String password) {
        return Member.builder()
                .email("test@test.com")
                .nickname("test")
                .password(password)
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().build())
                .enabled(true)
                .build();
    }
}
