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
		"요청 데이터 형식 오류",
		"B001"
	),

	MEMBER_SEQ_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"MEMBER SEQ 오류",
		"MSB002"
	),

	SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"SCHEDULE SEQ 오류",
		"SSB003"
	),

	MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"일정에 존재하지 않는 MEMBER SEQ입니다.",
		"MSNISB004"
	),

	PASSWORD_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"PASSWORD 오류",
		"PB005"
	),

	SCHEDULE_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"회원이 생성하지 않은 일정입니다.",
		"SDCBMB008"
	),

	EMAIL_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"EMAIL 오류",
		"EB009"
	),

	CODE_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"CODE 오류",
		"CB011"
	),

	PASSWORD_MISMATCH_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"비밀번호가 일치하지 않습니다.",
		"PMB013"
	),

	SELF_FRIEND_REQUEST_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"자기 자신에게 친추를 보낼 수 없습니다.",
		"SFRB015"
	),

	ALREADY_SENT_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"이미 전송 되어 있습니다.",
		"ASB017"
	),

	ALREADY_SENT_BY_FRIEND_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"친구가 이미 친추를 보냈습니다.",
		"ASBFB018"
	),

	FEED_SEQ_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"FEED SEQ 오류",
		"FSB019"
	),

	FEED_DIDNT_CREATED_BY_MEMBER_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"회원이 생성한 피드가 아닙니다.",
		"FDCBMB020"
	),

	MEMBER_CODE_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"MEMBER CODE 오류",
		"MCB021"
	),

	SENDER_SEQ_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"SENDER SEQ 오류",
		"SEB022"
	),

	RECEIVER_SEQ_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"RECEIVER SEQ 오류",
		"REB023"
	),

	FRIENDREQUEST_SEQ_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"FRIEND SEQ 오류",
		"FSB026"
	),

	BOOK_MARK_SEQ_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"BOOK MARK SEQ 오류",
		"BMSB024"
	),

	LOCATION_SEQ_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"LOCATION SEQ 오류",
		"LSB025"
	),

	SENDER_SEQ_MISMATCH_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"SENDER SEQ가 일치하지 않습니다.",
		"SMB027"
	),

	RECEIVER_SEQ_MISMATCH_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"RECEIVER SEQ가 일치하지 않습니다.",
		"RMB028"
	),

	START_TIME_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"START TIME 오류",
		"STB029"
	),

	HIDE_FEED_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"HIDE FEED SEQ 오류",
		"HFB030"
	),

	CHAT_ROOM_SEQ_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"CHAT ROOM SEQ 오류",
		"CRIB031"
	),

	CHAT_ROOM_DONT_HAVE_MEMBER_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"채팅방에 해당 Member가 존재하지 않는 오류",
		"CRDHMB032"
	),

	MEMBER_CREATED_SCHEDULE_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"회원이 생성한 일정입니다.",
		"MCSB033"
	),

	MEMBER_ALREADY_ACCEPT_SCHEDULE_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"회원이 이미 일정을 수락했습니다.",
		"MAASB034"
	),

	FEED_IMAGE_SIZE_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"피드 이미지와 FeedImageOrder의 수가 맞지 않습니다.",
		"FESB035"
	),

	COORDINATE_TIME_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"해당 일정의 좌표 정보를 가져올 시간이 아닙니다.",
		"CTB036"
	),

	TOKEN_MISMATCH_BAD_REQUEST_EXCEPTION(
			HttpStatus.BAD_REQUEST.value(),
			"이전 토큰 입니다.",
			"TMB037"
	),

	SCHEDULE_ALL_DAY_BAD_REQUEST_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"하루 종일 일정일 경우 위치를 확인할 수 없습니다.",
		"SADB038"
	),

	IN_ONE_HOUR_RANGE_SCHEDULE_BAD_REQUEST_EXCEPTION(
			HttpStatus.BAD_REQUEST.value(),
			"1시간 이내 일정은 멤버를 수정 할 수 없습니다.",
			"IOHRSB038"
	),

	// NOT FOUND
	HIDE_FEED_NOT_FOUND_EXCEPTION(
		HttpStatus.NOT_FOUND.value(),
		"HIDE FEED가 존재하지 않습니다.",
		"HFN001"
	),

	FRIEND_NOT_FOUND_EXCEPTION(
		HttpStatus.NOT_FOUND.value(),
		"해당 친구가 존재하지 않습니다.",
		"FN002"
	),

	CHAT_ROOM_DONT_HAVE_MEMBER_NOT_FOUND_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"CHAT ROOM에 MEMBER가 존재하지 않습니다.",
		"CRDHMN003"
	),

	CHAT_ROOM_NOT_FOUND_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"CHAT ROOM이 존재하지 않습니다.",
		"CRN004"
	),

	BOOK_MARK_FEED_NOT_FOUND_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"BOOK MARK한 피드가 존재하지 않습니다.",
		"BMFN004"
	),

	COORDINATE_NOT_FOUND_EXCEPTION(
		HttpStatus.BAD_REQUEST.value(),
		"좌표가 존재하지 않습니다.",
		"CN005"
	),

	TOKEN_NOT_FOUND_EXCEPTION(
			HttpStatus.BAD_REQUEST.value(),
			"존재 하지 않은 토큰 입니다.",
			"TN006"
	),

	KAKAO_NOT_FOUND_EXCEPTION(
			HttpStatus.BAD_REQUEST.value(),
			"카카오 회원가입이 필요합니다.",
			"KN007"
	),

	APPLE_NOT_FOUND_EXCEPTION(
			HttpStatus.BAD_REQUEST.value(),
			"애플 회원가입이 필요합니다.",
			"AN008"
	),

	// CONFLICT
	USER_ID_DUPLICATION_CONFLICT_EXCEPTION(
		HttpStatus.CONFLICT.value(),
		"USER ID 중복 오류",
		"UIDC001"
	),

	EMAIL_DUPLICATION_CONFLICT_EXCEPTION(
		HttpStatus.CONFLICT.value(),
		"EMAIL 중복 오류",
		"EDC002"
	),

	HIDE_FEED_DUPLICATION_CONFLICT_EXCEPTION(
		HttpStatus.CONFLICT.value(),
		"HIDE FEED 중복 오류",
		"HFEC003"
	),

	FEED_DUPLICATION_CONFLICT_EXCEPTION(
		HttpStatus.CONFLICT.value(),
		"FEED 중복 오류",
		"FDC004"
	),

	BOOK_MARK_FEED_DUPLICATION_CONFLICT_EXCEPTION(
		HttpStatus.CONFLICT.value(),
		"BOOK MARK FEED 중복 오류",
		"BMFDC005"
	),

	CHAT_ROOM_MEMBER_DUPLICATION_CONFLICT_EXCEPTION(
		HttpStatus.CONFLICT.value(),
		"CHAT ROOM MEMBER 중복 오류",
		"CRMDC006"
	),

	CHAT_ROOM_DUPLICATION_CONFLICT_EXCEPTION(
		HttpStatus.CONFLICT.value(),
		"CHAT ROOM 중복 오류",
		"CRDC007"
	),

	KAKAO_DUPLICATION_CONFLICT_EXCEPTION(
			HttpStatus.CONFLICT.value(),
			"Kakao Id 중복 오류",
			"KDCE008"

	),

	APPLE_DUPLICATION_CONFLICT_EXCEPTION(
			HttpStatus.CONFLICT.value(),
			"Apple Id 중복 오류",
			"ADCE009"

	),

	ALREADY_FRIEND_CONFLICT_EXCEPTION(
			HttpStatus.CONFLICT.value(),
			"이미 친구 입니다.",
			"AFCE010"
	),

	// UNAUTHORIZED
	EXPIRED_TOKEN_UNAUTHORIZED_EXCEPTION(
			HttpStatus.UNAUTHORIZED.value(),
			"만료된 토큰",
			"ETU001"
	),


	// SERVER
	UNKNOWN_EXCEPTION(
		HttpStatus.INTERNAL_SERVER_ERROR.value(),
		"서버 오류",
		"S500"
	),

	FIREBASE_CLOUD_MESSAGING_EXCEPTION(
		HttpStatus.INTERNAL_SERVER_ERROR.value(),
		"FIREBASE CLOUD MESSAGING 서버 오류",
		"S501"
	),

	;

	private final int httpStatus;
	private final String message;
	private final String code;
}
