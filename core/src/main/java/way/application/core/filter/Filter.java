package way.application.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import way.application.utils.log.entity.LogEntity;
import way.application.utils.log.repository.LogJpaRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Filter implements jakarta.servlet.Filter {

    private final LogJpaRepository requestLogRepository;

    private static final String START_TIME = "startTime";

    private final List<String> excludeUrls = List.of(
            "/v3/api-docs", "/v3/api-docs/swagger-config",
            "/swagger-ui/index.html", "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/index.css", "/swagger-ui/swagger-initializer.js",
            "/swagger-ui/swagger-ui-bundle.js", "/swagger-ui/swagger-ui.css",
            "/admin/logs","/actuator/health","/index.html","/","/swagger-ui/favicon-32x32.png","/error",
            "/private-policy","/swagger-ui/favicon-16x16.png"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest((HttpServletRequest) request);
        CachedBodyHttpServletResponse cachedResponse = new CachedBodyHttpServletResponse((HttpServletResponse) response);

        // 필터 체인 실행
        chain.doFilter(multiReadRequest, cachedResponse);

        // 응답 상태 코드가 200일 경우에만 로그 처리
        if (cachedResponse.getStatus() == HttpServletResponse.SC_OK) {
            long startTime = (long) request.getAttribute(START_TIME);
            String duration = System.currentTimeMillis() - startTime + "ms";

            String url = ((HttpServletRequest) request).getRequestURI();
            int status = ((HttpServletResponse) response).getStatus();

            String responseBody = cachedResponse.getBody();

            String requestBody = getRequestBody(request);
            Map<String, String> requestParams = getRequestParams(request);

            String logDetail = !requestBody.isEmpty() ? "Body: " + requestBody : "Params: " + requestParams;

            if(!excludeUrls.contains(url)) {

                LogEntity logEntity = LogEntity.builder()
                        .httpStatus(status)
                        .requestUri(url)
                        .message(responseBody)
                        .errorCode("SUCCESS")
                        .exception("SUCCESS")
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .param(logDetail)
                        .build();

                requestLogRepository.save(logEntity);
            }

        }

        // 최종적으로 실제 응답에 바디를 써주기 (안 하면 클라이언트가 응답 못 받음)
        response.getOutputStream().write(cachedResponse.getBody().getBytes(response.getCharacterEncoding()));
    }

    private String getRequestBody(ServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    // ✅ 요청 파라미터(Query Params) 가져오기
    private Map<String, String> getRequestParams(ServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            paramMap.put(paramName, request.getParameter(paramName));
        }
        return paramMap;
    }
}
