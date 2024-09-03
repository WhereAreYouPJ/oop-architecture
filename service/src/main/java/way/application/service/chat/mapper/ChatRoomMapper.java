package way.application.service.chat.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.chatRoom.entity.ChatRoomEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatRoomMapper {
	@Mapping(target = "chatRoomSeq", source = "chatRoomSeq")
	@Mapping(target = "scheduleEntity", source = "scheduleEntity")
	ChatRoomEntity toChatRoomEntity(String chatRoomSeq, ScheduleEntity scheduleEntity);
}
