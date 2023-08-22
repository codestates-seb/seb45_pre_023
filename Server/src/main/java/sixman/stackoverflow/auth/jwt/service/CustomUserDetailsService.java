package sixman.stackoverflow.auth.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.domain.member.repository.MemberRepository;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    
        Member member = findByEmail(email);

        checkEnableMember(member);

        return createUserDetails(member);
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));
    }

    private void checkEnableMember(Member member) {
        if(!member.isEnabled()) throw new DisabledException("탈퇴한 회원입니다.");
    }

    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new CustomUserDetails(
                member.getMemberId(),
                member.getEmail(),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
