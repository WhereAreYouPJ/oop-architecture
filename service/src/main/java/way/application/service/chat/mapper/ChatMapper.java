package way.application.service.chat.mapper;

import static way.application.service.chat.dto.response.ChatResponseDto.*;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.mongo.chat.documents.ChatEntity;
import way.application.service.chat.dto.response.ChatResponseDto.GetChatResponseDto;
import way.application.service.chat.dto.response.ChatResponseDto.MessageDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper {
	@Mapping(target = "chatSeq", ignore = true)
	@Mapping(target = "chatRoomEntity", source = "chatRoomEntity")
	@Mapping(target = "memberEntity", source = "memberEntity")
	@Mapping(target = "message", source = "message")
	ChatEntity toChatEntity(ChatRoomEntity chatRoomEntity, MemberEntity memberEntity, String message);

	@Mapping(target = "memberSeq", source = "memberEntity.memberSeq")
	@Mapping(target = "userName", source = "memberEntity.userName")
	@Mapping(target = "message", source = "message")
	MessageDto toMessageDto(ChatEntity chatEntity);

	@Mapping(target = "memberSeq", source = "memberSeq")
	@Mapping(target = "userName", source = "userName")
	OwnerDto toOwnerDto(MemberEntity memberEntity);

	default GetChatResponseDto toGetChatResponseDto(MemberEntity memberEntity, List<ChatEntity> chatEntities) {
		OwnerDto ownerDto = toOwnerDto(memberEntity);

		List<MessageDto> messageInfos = chatEntities.stream()
			.map(this::toMessageDto)
			.collect(Collectors.toList());
		return new GetChatResponseDto(ownerDto, messageInfos);
	}
}
