package way.application.utils.log.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import way.application.utils.log.entity.LogEntity;
import way.application.utils.log.repository.LogJpaRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestLoggingInterceptor implements HandlerInterceptor {
    private final LogJpaRepository requestLogRepository;

    private final List<String> excludeUrls = List.of(
            "/v3/api-docs", "/v3/api-docs/swagger-config",
            "/swagger-ui/index.html", "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/index.css", "/swagger-ui/swagger-initializer.js",
            "/swagger-ui/swagger-ui-bundle.js", "/swagger-ui/swagger-ui.css",
            "/admin/logs","/actuator/health","/index.html","/","/swagger-ui/favicon-32x32.png","/error"
    );


    private static final String START_TIME = "startTime";
    private static final String REQUEST_BODY = "requestBody";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        long startTime = (long) request.getAttribute(START_TIME);
        String duration = System.currentTimeMillis() - startTime + "ms";

        String url = request.getRequestURI();
        int status = response.getStatus();

        if(!excludeUrls.contains(url)) {
            LogEntity logEntity = LogEntity.builder()
                    .httpStatus(status)
                    .requestUri(url)
                    .message(duration)
                    .errorCode("SUCCESS")
                    .exception("SUCCESS")
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .build();

            requestLogRepository.save(logEntity);
        }
    }
}

