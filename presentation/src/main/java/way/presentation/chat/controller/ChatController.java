package way.presentation.chat.controller;

import static way.application.service.chat.dto.request.ChatRoomRequestDto.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import way.application.service.chat.dto.response.ChatResponseDto.GetChatResponseDto;
import way.application.service.chat.service.ChatRoomService;
import way.application.utils.exception.GlobalExceptionHandler;
import way.presentation.base.BaseResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "채팅방", description = "담당자 (박종훈)")
public class ChatController {
	private final SimpMessageSendingOperations template;
	private final ChatRoomService chatRoomService;

	@MessageMapping("/sendMessage")
	public void sendMessage(@Payload SendChatRequestDto chat) {
		log.info("sendMessage = {}", chat.message());

		chatRoomService.createChat(chat);

		template.convertAndSend("/sub/chat/room/" + chat.chatRoomSeq(), chat);
	}

	@GetMapping(value = "/chat/message", name = "채팅 내용 조회")
	@Operation(summary = "채팅 내용 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "200 요청에 성공하였습니다.", useReturnTypeSchema = true),
		@ApiResponse(responseCode = "S500", description = "500 서버 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "MSB002", description = "400 MEMBER SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "CRIB031", description = "400 CHAT ROOM SEQ 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
		@ApiResponse(responseCode = "CRDHMB032", description = "400 채팅방에 해당 Member가 존재하지 않는 오류", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
	})
	public ResponseEntity<BaseResponse<GetChatResponseDto>> getChatMessage(
		@RequestParam(name = "memberSeq") Long memberSeq,
		@RequestParam(name = "chatRoomSeq") String chatRoomSeq
	) {
		GetChatResponseDto response = chatRoomService.getChatMessage(memberSeq, chatRoomSeq);

		return ResponseEntity.ok().body(BaseResponse.ofSuccess(HttpStatus.OK.value(), response));
	}
}