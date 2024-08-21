package way.application.service.bookMark.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMarkMapper {
	@Mapping(target = "bookMarkSeq", ignore = true)
	@Mapping(target = "feedEntity", source = "feedEntity")
	@Mapping(target = "memberEntity", source = "memberEntity")
	BookMarkEntity toBookMarkEntity(FeedEntity feedEntity, MemberEntity memberEntity);
}
