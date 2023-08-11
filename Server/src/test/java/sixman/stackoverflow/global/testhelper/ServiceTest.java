package sixman.stackoverflow.global.testhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;

@Transactional
@SpringBootTest
@ActiveProfiles("local")
public abstract class ServiceTest {

    @Autowired protected PasswordEncoder passwordEncoder;

    protected Member createMember() {
        return Member.builder()
                .email("test@test.com")
                .nickname("test")
                .password(passwordEncoder.encode("1234abcd!"))
                .authority(Authority.ROLE_USER)
                .myInfo(MyInfo.builder().build())
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
