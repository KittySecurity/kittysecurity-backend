package pl.edu.pk.student.kittysecurity.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class HttpLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(HttpLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        long start = System.currentTimeMillis();

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String endpoint = req.getRequestURI();
        String method = req.getMethod();
        String source = req.getServletPath();

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            logger.error("[{}] [{}] [{}] [{}] [{}ms] [{}]",
                    Instant.now(), source, method, endpoint, duration, e.getMessage());
            throw e;
        }

        long duration = System.currentTimeMillis() - start;

        int status = res.getStatus();
        String message = status >= 400 ? "Request failed" : "Request completed";

        logger.info("[{}] [{}] [{}] [{}] [{}] [{}] [{}ms]",
                Instant.now(), source, method, endpoint, status, message, duration);
    }
}