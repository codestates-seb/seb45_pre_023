package sixman.stackoverflow.global.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class MDCLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        UUID uuid = UUID.randomUUID();
        MDC.put("request_id", uuid.toString());
        MDC.put("email", "anonymous");

        long startTime = System.currentTimeMillis();

        chain.doFilter(request, response);

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        String method = ((HttpServletRequest) request).getMethod();
        String url = ((HttpServletRequest) request).getRequestURI().split("/")[1];

        log.info("{} {}{} duration: {} ms", method, "/", url, duration);

        MDC.clear();
    }
}
