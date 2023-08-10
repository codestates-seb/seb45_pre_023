package sixman.stackoverflow.global.testhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sixman.stackoverflow.domain.member.entity.Authority;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.entity.MyInfo;

import javax.persistence.EntityManager;

@DataJpaTest
public abstract class RepositoryTest {

    @Autowired protected EntityManager em;
    protected PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
}
