package way.application.service.chat.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomMemberEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatRoomMemberMapper {
	@Mapping(target = "chatRoomMemberSeq", ignore = true)
	@Mapping(target = "memberEntity", source = "memberEntity")
	@Mapping(target = "chatRoomEntity", source = "chatRoomEntity")
	@Mapping(target = "scheduleEntity", source = "scheduleEntity")
	ChatRoomMemberEntity toChatRoomMemberEntity(
		MemberEntity memberEntity,
		ChatRoomEntity chatRoomEntity,
		ScheduleEntity scheduleEntity
	);
}
