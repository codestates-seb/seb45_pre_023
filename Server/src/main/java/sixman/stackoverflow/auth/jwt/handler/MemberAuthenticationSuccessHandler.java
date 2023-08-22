package sixman.stackoverflow.auth.jwt.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import sixman.stackoverflow.auth.jwt.service.CustomUserDetails;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MemberAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Long memberId = userDetails.getMemberId();

        setMemberIdInResponse(response, memberId);


    }

    private void setMemberIdInResponse(HttpServletResponse response, Long memberId) throws IOException {

        String jsonResponse = "{\"memberId\":" + memberId + "}";

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
