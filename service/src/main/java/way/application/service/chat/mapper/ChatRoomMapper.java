package way.application.service.chat.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatRoomMapper {
	@Mapping(target = "chatRoomSeq", source = "chatRoomId")
	@Mapping(target = "roomName", source = "roomName")
	ChatRoomEntity toChatRoomEntity(String chatRoomId, String roomName);
}
