package sixman.stackoverflow.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sixman.stackoverflow.domain.member.entity.Member;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
@Profile({"production"})
public class Init {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.init();
    }

    @Component
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;

        @Transactional
        public void init() {
            Member member = Member.createMember(
                    "test@google.com",
                    "test",
                    passwordEncoder.encode("1q2w3e4r!"));
            em.persist(member);
        }
    }
}
