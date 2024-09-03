package way.presentation.chat.controller;

import static way.application.service.chat.dto.response.ChatRoomResponseDto.*;
import static way.presentation.chat.vo.request.ChatRoomRequestVo.*;
import static way.presentation.chat.vo.response.ChatRoomResponseVo.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import way.application.service.chat.service.ChatRoomService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;
import way.presentation.chat.validates.CreateChatRoomValidator;
import way.presentation.chat.validates.EnterChatRoomValidator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-room")
@Tag(name = "채팅", description = "담당자 (박종훈)")
public class ChatRoomController {
	private final CreateChatRoomValidator createChatRoomValidator;
	private final EnterChatRoomValidator enterChatRoomValidator;

	private final ChatRoomService chatRoomService;

	// 채팅방 생성
	@PostMapping()
	@Operation(summary = "채팅방 생성 API", description = "일정 생성될 시 채팅방 생성 API 입니다. 순수 채팅방 생성 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "요청에 성공하였습니다.",
			useReturnTypeSchema = true),
		@ApiResponse(
			responseCode = "S500",
			description = "500 SERVER_ERROR",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "B001",
			description = "400 Invalid DTO Parameter errors",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "SSB003",
			description = "400 SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION / Schedule Seq 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "CRDC007",
			description = "409 CHAT_ROOM_DUPLICATION_CONFLICT_EXCEPTION / 이미 해당 Schedule 의 Chat Room 존재",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse<CreateChatRoomResponse>> createChatRoom(
		@Valid
		@RequestBody CreateChatRoomRequest request
	) {
		// 유효성 검사
		createChatRoomValidator.validate(request);

		CreateChatRoomResponseDto createChatRoomResponseDto
			= chatRoomService.createChatRoom(request.toCreateChatRoomRequestDto());

		CreateChatRoomResponse response = new CreateChatRoomResponse(createChatRoomResponseDto.chatRoomSeq());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}

	// 채팅방 입장
	@PostMapping("/enter")
	@Operation(summary = "채팅방 입장 API", description = "채팅방 생성 이후, Schedule Accept true API 이후 채팅방 입장")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "요청에 성공하였습니다.",
			useReturnTypeSchema = true),
		@ApiResponse(
			responseCode = "S500",
			description = "500 SERVER_ERROR",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "B001",
			description = "400 Invalid DTO Parameter errors",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "MSB002",
			description = "400 MEMBER_SEQ_BAD_REQUEST_EXCEPTION / Member Seq 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "SSB003",
			description = "400 SCHEDULE_SEQ_BAD_REQUEST_EXCEPTION / Schedule Seq 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "MSNISB004",
			description = "400 MEMBER_SEQ_NOT_IN_SCHEDULE_BAD_REQUEST_EXCEPTION / 해당 Member 가 Schedule 에 존재하지 않을 때 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "CRSB031",
			description = "400 CHAT_ROOM_SEQ_BAD_REQUEST_EXCEPTION / Chat Room Seq 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(
			responseCode = "CRMDC006",
			description = "409 CHAT_ROOM_MEMBER_DUPLICATION_CONFLICT_EXCEPTION / 이미 채팅방에 존재할 때 오류",
			content = @Content(
				schema = @Schema(
					implementation = GlobalExceptionHandler.ErrorResponse.class))),
	})
	public ResponseEntity<BaseResponse> enterChatRoom(
		@Valid
		@RequestBody EnterChatRoomRequest request
	) {
		// 유효성 검사
		enterChatRoomValidator.validate(request);

		chatRoomService.enterChatRoom(request.toEnterChatRoomRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}