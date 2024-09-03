package way.presentation.chat.controller;

import static way.application.service.chat.dto.request.ChatRoomRequestDto.*;
import static way.presentation.chat.vo.request.ChatRoomRequestVo.*;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import way.application.infrastructure.mongo.chat.repository.ChatRepository;
import way.application.service.chat.dto.request.ChatRoomRequestDto;
import way.application.service.chat.service.ChatRoomService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {
	private final SimpMessageSendingOperations template;
	private final ChatRoomService chatRoomService;

	@MessageMapping("/sendMessage")
	public void sendMessage(@Payload SendChatRequestDto chat) {
		chatRoomService.createChat(chat);

		template.convertAndSend("/sub/chat/room/" + chat.chatRoomSeq(), chat);
	}
}