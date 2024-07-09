package way.application.utils.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorResult {

	// BAD_REQUEST
	DTO_BAD_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "BTO Bad Request Exception", "B001"),
	MEMBER_SEQ_BAD_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "MEMBER_SEQ_BAD_REQUEST_EXCEPTION", "MSB002"),

	// CONFLICT

	// SERVER
	UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Exception", "S500"),
	FIREBASE_CLOUD_MESSAGING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "FIREBASE_CLOUD_MESSAGING_EXCEPTION", "S501"),
	;

	private final HttpStatus httpStatus;
	private final String message;
	private final String code;
}
