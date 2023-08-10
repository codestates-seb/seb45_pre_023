package sixman.stackoverflow.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import sixman.stackoverflow.global.exception.businessexception.BusinessException;
import sixman.stackoverflow.global.response.ApiSingleResponse;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthUtil {

    public static void sendErrorResponse(HttpServletResponse response, BusinessException exception) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(exception.getHttpStatus().value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ApiSingleResponse.fail(exception)));
    }
}
