package way.presentation.chat.controller;

import static way.presentation.chat.vo.request.ChatRoomRequestVo.*;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import way.application.service.chat.service.ChatRoomService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {
	private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
	private final static String CHAT_QUEUE_NAME = "chat.queue";
	private final RabbitTemplate rabbitTemplate;
	private final ChatRoomService chatRoomService;

	// /pub/chat.message.{roomId} 로 요청하면 브로커를 통해 처리
	// /exchange/chat.exchange/room.{roomId} 를 구독한 클라이언트에 메시지가 전송된다.
	@MessageMapping("chat.message.{chatRoomId}")
	public void sendMessage(@Payload SendChatRequest request, @DestinationVariable String chatRoomId) {
		chatRoomService.createChat(request.toSendChatRequestDto());

		rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, request);
	}

	//기본적으로 chat.queue가 exchange에 바인딩 되어있기 때문에 모든 메시지 처리
	@RabbitListener(queues = CHAT_QUEUE_NAME)
	public void receive(SendChatRequest request) {
		System.out.println("received : " + request.message());
	}
}