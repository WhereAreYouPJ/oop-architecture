package way.application.service.bookMark.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMarkMapper {
	@Mapping(target = "bookMarkSeq", ignore = true)
	@Mapping(target = "scheduleEntity", source = "scheduleEntity")
	@Mapping(target = "memberEntity", source = "memberEntity")
	BookMarkEntity toBookMarkEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity);
}
