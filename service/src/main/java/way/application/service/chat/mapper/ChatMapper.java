package way.application.service.chat.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.mongo.chat.documents.ChatEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper {
	@Mapping(target = "chatSeq", ignore = true)
	@Mapping(target = "chatRoomEntity", source = "chatRoomEntity")
	@Mapping(target = "memberEntity", source = "memberEntity")
	@Mapping(target = "message", source = "message")
	ChatEntity toChatEntity(ChatRoomEntity chatRoomEntity, MemberEntity memberEntity, String message);
}
