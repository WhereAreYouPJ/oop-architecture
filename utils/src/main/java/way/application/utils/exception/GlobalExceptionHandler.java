package way.application.utils.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import way.application.utils.log.event.LogEvent;

import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private final ApplicationEventPublisher eventPublisher;

	// ✅ 공통 예외 처리
	@ExceptionHandler({Exception.class})
	public ResponseEntity<ErrorResponse> handleException(
			final Exception exception,
			HttpServletResponse response,
			HttpServletRequest request
	) {
		log.warn("⚠️ Server Exception occurred: ", exception);

		// 요청 데이터 가져오기
		String requestBody = getRequestBody(request);
		Map<String, String> requestParams = getRequestParams(request);

		// body 또는 param 중 하나만 포함
		String logDetail;
		if (!requestBody.isEmpty()) {
			logDetail = "Body: " + requestBody;
		} else if (!requestParams.isEmpty()) {
			logDetail = "Params: " + requestParams;
		} else {
			logDetail = "No Params or Body";
		}

		// LogEvent 발행 (요청 데이터 포함)
		eventPublisher.publishEvent(new LogEvent(
				500, "서버 오류", "S500", "UNKNOWN_EXCEPTION",
				request.getRequestURI(), logDetail
		));

		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return this.makeErrorResponseEntity(ErrorResult.UNKNOWN_EXCEPTION);
	}

	@ExceptionHandler({BadRequestException.class})
	public ResponseEntity<ErrorResponse> handleBadRequestException(
			final BadRequestException exception,
			HttpServletResponse response,
			HttpServletRequest request
	) {
		log.warn("⚠️ BadRequest Exception occurred: ", exception);

		String requestBody = getRequestBody(request);
		Map<String, String> requestParams = getRequestParams(request);

		String logDetail = !requestBody.isEmpty() ? "Body: " + requestBody : "Params: " + requestParams;

		eventPublisher.publishEvent(new LogEvent(
				exception.getErrorResult().getHttpStatus(),
				exception.getErrorResult().getMessage(),
				exception.getErrorResult().getCode(),
				exception.getErrorResult().toString(),
				request.getRequestURI(),
				logDetail
		));

		response.setStatus(exception.getErrorResult().getHttpStatus());
		return this.makeErrorResponseEntity(exception.getErrorResult());
	}

	@ExceptionHandler({NotFoundRequestException.class})
	public ResponseEntity<ErrorResponse> handleNotFoundException(
			final NotFoundRequestException exception,
			HttpServletResponse response,
			HttpServletRequest request
	) {
		log.warn("⚠️ Not Found Exception occurred: ", exception);

		String requestBody = getRequestBody(request);
		Map<String, String> requestParams = getRequestParams(request);

		String logDetail = !requestBody.isEmpty() ? "Body: " + requestBody : "Params: " + requestParams;

		eventPublisher.publishEvent(new LogEvent(
				exception.getErrorResult().getHttpStatus(),
				exception.getErrorResult().getMessage(),
				exception.getErrorResult().getCode(),
				exception.getErrorResult().toString(),
				request.getRequestURI(),
				logDetail
		));

		response.setStatus(exception.getErrorResult().getHttpStatus());
		return this.makeErrorResponseEntity(exception.getErrorResult());
	}

	@ExceptionHandler({ConflictException.class})
	public ResponseEntity<ErrorResponse> handleConflictException(
			final ConflictException exception,
			HttpServletResponse response,
			HttpServletRequest request
	) {
		log.warn("⚠️ Conflict Exception occurred: ", exception);

		String requestBody = getRequestBody(request);
		Map<String, String> requestParams = getRequestParams(request);

		String logDetail = !requestBody.isEmpty() ? "Body: " + requestBody : "Params: " + requestParams;

		eventPublisher.publishEvent(new LogEvent(
				exception.getErrorResult().getHttpStatus(),
				exception.getErrorResult().getMessage(),
				exception.getErrorResult().getCode(),
				exception.getErrorResult().toString(),
				request.getRequestURI(),
				logDetail
		));

		response.setStatus(exception.getErrorResult().getHttpStatus());
		return this.makeErrorResponseEntity(exception.getErrorResult());
	}

	private String getRequestBody(HttpServletRequest request) {
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

	private Map<String, String> getRequestParams(HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<>();
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			paramMap.put(paramName, request.getParameter(paramName));
		}
		return paramMap;
	}

	// ✅ 공통 응답 생성
	private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final ErrorResult errorResult) {
		return ResponseEntity.status(errorResult.getHttpStatus())
				.body(new ErrorResponse(errorResult.getHttpStatus(), errorResult.getMessage(), errorResult.getCode()));
	}

	@Getter
	@RequiredArgsConstructor
	public static class ErrorResponse {
		private final int status;
		private final String message;
		private final String code;
	}
}
