package way.application.utils.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorResult {

	// BAD_REQUEST
	DTO_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"BTO Bad Request Exception",
		"B001"
	),

	MEMBER_SEQ_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"MEMBER_SEQ_BAD_REQUEST_EXCEPTION",
		"MSB002"
	),

	SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION",
		"SSB003"
	),

	MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION",
		"MSNISB004"
	),

	PASSWORD_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"PASSWORD_BAD_REQUEST_EXCEPTION",
		"PB005"
	),

	SCHEDULE_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"SCHEDULE_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION",
		"SDCBMB008"
	),

	EMAIL_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"EMAIL_BAD_REQUEST_EXCEPTION",
		"EB009"
	),

	CODE_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"CODE_BAD_REQUEST_EXCEPTION",
		"CB011"
	),

	PASSWORD_MISMATCH_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"PASSWORD_MISMATCH_BAD_REQUEST_EXCEPTION",
		"PMB013"
	),

	FEED_SEQ_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"FEED_SEQ_BAD_REQUEST_EXCEPTION",
		"FSB019"
	),

	FEED_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"FEED_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION",
		"FDCBMB020"
	),

	// CONFLICT
	USER_ID_DUPLICATION_CONFLICT_EXCEPTION(
		HttpStatus.CONFLICT.value(),
		"USER_ID_DUPLICATION_CONFLICT_EXCEPTION",
		"UIDC001"
	),

	EMAIL_DUPLICATION_CONFLICT_EXCEPTION(
		HttpStatus.CONFLICT.value(),
		"EMAIL_DUPLICATION_CONFLICT_EXCEPTION",
		"EDC002"
	),

	HIDE_FEED_DUPLICATION_CONFLICT_EXCEPTION(
		HttpStatus.CONFLICT.value(),
		"HIDE_FEED_DUPLICATION_CONFLICT_EXCEPTION",
		"HFEC003"
	),

	FEED_DUPLICATION_CONFLICT_EXCEPTION(
		HttpStatus.CONFLICT.value(),
		"FEED_DUPLICATION_CONFLICT_EXCEPTION",
		"FDC004"
	),

	// SERVER
	UNKNOWN_EXCEPTION(
		HttpStatus.INTERNAL_SERVER_ERROR.value(),
		"Unknown Exception",
		"S500"
	),

	FIREBASE_CLOUD_MESSAGING_EXCEPTION(
		HttpStatus.INTERNAL_SERVER_ERROR.value(),
		"FIREBASE_CLOUD_MESSAGING_EXCEPTION",
		"S501"
	),

	;

	private final int httpStatus;
	private final String message;
	private final String code;
}
