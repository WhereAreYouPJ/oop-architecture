package way.application.core.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import way.application.utils.log.aop.RequestLoggingInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final RequestLoggingInterceptor requestLoggingInterceptor;

	private static final String CORS_URL_PATTERN = "/*";
	private static final String CORS_URL = "*";
	private static final String CORS_METHOD = "*";

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping(CORS_URL_PATTERN)
				.allowedOrigins(CORS_URL)
				.allowedMethods(CORS_METHOD);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestLoggingInterceptor)
				.addPathPatterns("/**"); // 전역에 적용
	}
}
