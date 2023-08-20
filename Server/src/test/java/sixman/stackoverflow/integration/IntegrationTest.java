package sixman.stackoverflow.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import sixman.stackoverflow.auth.jwt.service.CustomUserDetails;
import sixman.stackoverflow.auth.jwt.service.TokenProvider;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.module.aws.service.S3Service;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationTest {

    @Autowired private TokenProvider tokenProvider;
    @Autowired protected MockMvc mockMvc;

    private String createAccessToken(Member member, long accessTokenExpireTime) {
        UserDetails userDetails = createUserDetails(member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        return tokenProvider.generateAccessToken(authenticationToken, accessTokenExpireTime);
    }

    private String createRefreshToken(Member member, long refreshTokenExpireTime) {
        UserDetails userDetails = createUserDetails(member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        return tokenProvider.generateRefreshToken(authenticationToken, refreshTokenExpireTime);
    }

    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new CustomUserDetails(
                member.getMemberId(),
                String.valueOf(member.getEmail()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
