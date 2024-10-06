package way.presentation.chat.controller;

import static way.presentation.chat.vo.request.ChatRoomRequestVo.*;

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
import lombok.RequiredArgsConstructor;
import way.application.service.chat.service.ChatRoomService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-room")
@Tag(name = "채팅", description = "담당자 (박종훈)")
public class ChatRoomController {

	private final ChatRoomService chatRoomService;

	@PostMapping("/enter")
	@Operation(summary = "채팅방 입장 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "B001", description = "400 요청 데이터 형식 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "SSB003", description = "400 SCHEDULE SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSNISB004", description = "400 일정에 존재하지 않는 MEMBER SEQ입니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "CRSB031", description = "400 CHAT ROOM SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "CRMDC006", description = "409 이미 채팅방에 존재하는 멤버입니다.", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse> enterChatRoom(@RequestBody EnterChatRoomRequest request) {
		request.validateEnterChatRoomRequest();

		chatRoomService.enterChatRoom(request.toEnterChatRoomRequestDto());

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), "SUCCESS"));
	}
}