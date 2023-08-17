package sixman.stackoverflow.global.testhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.auth.jwt.service.CustomUserDetails;
import sixman.stackoverflow.domain.answer.service.AnswerService;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;
import sixman.stackoverflow.domain.question.repository.QuestionRepository;
import sixman.stackoverflow.domain.question.service.QuestionService;
import sixman.stackoverflow.module.aws.service.S3Service;
import sixman.stackoverflow.module.email.service.MailService;
import sixman.stackoverflow.module.redis.service.RedisService;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

@Transactional
@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc
public abstract class ServiceTest {

    @MockBean protected S3Service s3Service;
    @MockBean protected RedisService redisService;
    @MockBean protected JavaMailSender emailSender;
    @Autowired protected AnswerService answerService;
    @Autowired protected QuestionService questionService;
    @Autowired protected QuestionRepository questionRepository;
    @Autowired protected PasswordEncoder passwordEncoder;
    @Autowired protected EntityManager em;

    protected Member createMember() {
        return Member.builder()
                .email("test@google.com")
                .nickname("test")
                .password(passwordEncoder.encode("1234abcd!"))
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder()
                        .myIntro("test intro")
                        .title("title")
                        .location("location")
                        .accounts(List.of("account1", "account2"))
                        .image("images/test.png")
                        .build())
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

    protected Member createMember(Long memberId) {
        return Member.builder()
                .memberId(memberId)
                .email("test@google.com")
                .nickname("test")
                .password("1234abcd!")
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().image("test url").build())
                .enabled(true)
                .build();
    }

    protected void setDefaultAuthentication(Long id){
        UserDetails userDetails = createUserDetails(id, createMember());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextImpl securityContext = new SecurityContextImpl(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
    }

    private UserDetails createUserDetails(Long id, Member notSavedmember) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(notSavedmember.getAuthority().toString());

        return new CustomUserDetails(
                id,
                String.valueOf(notSavedmember.getEmail()),
                notSavedmember.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
