package way.application.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.context.ApplicationEventPublisher;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import way.application.utils.log.event.LogEvent;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private final ApplicationEventPublisher eventPublisher;

	@ExceptionHandler({Exception.class})
	public ResponseEntity<ErrorResponse> handleException(
		final Exception exception,
		HttpServletResponse response
	) {
		log.warn("Server Exception occur: ", exception);

		eventPublisher.publishEvent(new LogEvent(500, "서버 오류 ", "S500", "UNKNOWN_EXCEPTION"));
		response.setStatus(500);

		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return this.makeErrorResponseEntity(ErrorResult.UNKNOWN_EXCEPTION);
	}

	@ExceptionHandler({BadRequestException.class})
	public ResponseEntity<ErrorResponse> handleBadRequestException(
		final BadRequestException exception,
		HttpServletResponse response
	) {
		log.warn("BadRequest Exception occur: ", exception);

		int httpStatus = exception.getErrorResult().getHttpStatus();
		String message = exception.getErrorResult().getMessage();
		String errorCode = exception.getErrorResult().getCode();
		String exceptionType = exception.getErrorResult().toString();
		eventPublisher.publishEvent(new LogEvent(httpStatus, message, errorCode, exceptionType));

		response.setStatus(httpStatus);
		return this.makeErrorResponseEntity(exception.getErrorResult());
	}

	@ExceptionHandler({NotFoundRequestException.class})
	public ResponseEntity<ErrorResponse> handleNotFoundException(
		final NotFoundRequestException exception,
		HttpServletResponse response
	) {
		log.warn("Not Found Exception occur: ", exception);

		int httpStatus = exception.getErrorResult().getHttpStatus();
		String message = exception.getErrorResult().getMessage();
		String errorCode = exception.getErrorResult().getCode();
		String exceptionType = exception.getErrorResult().toString();
		eventPublisher.publishEvent(new LogEvent(httpStatus, message, errorCode, exceptionType));

		response.setStatus(httpStatus);
		return this.makeErrorResponseEntity(exception.getErrorResult());
	}

	@ExceptionHandler({ConflictException.class})
	public ResponseEntity<ErrorResponse> handleConflictException(
		final ConflictException exception,
		HttpServletResponse response
	) {
		log.warn("Conflict Exception occur: ", exception);

		int httpStatus = exception.getErrorResult().getHttpStatus();
		String message = exception.getErrorResult().getMessage();
		String errorCode = exception.getErrorResult().getCode();
		String exceptionType = exception.getErrorResult().toString();
		eventPublisher.publishEvent(new LogEvent(httpStatus, message, errorCode, exceptionType));

		response.setStatus(httpStatus);
		return this.makeErrorResponseEntity(exception.getErrorResult());
	}

	@ExceptionHandler({UnauthorizedException.class})
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(
			final UnauthorizedException exception,
			HttpServletResponse response
	) {
		log.warn("Unauthorized Exception occur: ", exception);

		int httpStatus = exception.getErrorResult().getHttpStatus();
		String message = exception.getErrorResult().getMessage();
		String errorCode = exception.getErrorResult().getCode();
		String exceptionType = exception.getErrorResult().toString();
		eventPublisher.publishEvent(new LogEvent(httpStatus, message, errorCode, exceptionType));

		response.setStatus(httpStatus);
		return this.makeErrorResponseEntity(exception.getErrorResult());
	}

	private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final ErrorResult errorResult) {
		return ResponseEntity.status(errorResult.getHttpStatus())
			.body(
				new ErrorResponse(
					errorResult.getHttpStatus(),
					errorResult.getMessage(),
					errorResult.getCode())
			);
	}

	@Getter
	@RequiredArgsConstructor
	public static class ErrorResponse {
		private final int status;
		private final String message;
		private final String code;
	}
}
