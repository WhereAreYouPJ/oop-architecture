package way.application.service.hideFeed.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.hideFeed.entity.HideFeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HideFeedMapper {
	@Mapping(target = "hideFeedSeq", ignore = true)
	@Mapping(target = "scheduleEntity", source = "scheduleEntity")
	@Mapping(target = "memberEntity", source = "memberEntity")
	HideFeedEntity toHideFeedEntity(ScheduleEntity scheduleEntity, MemberEntity memberEntity);
}
