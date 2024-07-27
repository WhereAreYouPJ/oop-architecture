package way.application.utils.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotFoundRequestException extends RuntimeException {
	private final ErrorResult errorResult;
}
