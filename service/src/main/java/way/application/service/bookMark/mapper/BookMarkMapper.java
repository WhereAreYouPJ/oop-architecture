package way.application.service.bookMark.mapper;

import static way.application.service.bookMark.dto.response.BookMarkResponseDto.*;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import way.application.infrastructure.jpa.bookMark.entity.BookMarkEntity;
import way.application.infrastructure.jpa.feed.entity.FeedEntity;
import way.application.infrastructure.jpa.feedImage.entity.FeedImageEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMarkMapper {
	@Mapping(target = "bookMarkSeq", ignore = true)
	@Mapping(target = "feedEntity", source = "feedEntity")
	@Mapping(target = "memberEntity", source = "memberEntity")
	BookMarkEntity toBookMarkEntity(FeedEntity feedEntity, MemberEntity memberEntity);

	AddBookMarkResponseDto toAddBookMarkResponseDto(BookMarkEntity bookMarkEntity);

	default GetBookMarkResponseDto toGetBookMarkResponseDto(
		BookMarkEntity bookMarkEntity,
		List<FeedImageEntity> feedImageEntities,
		List<MemberEntity> memberEntities
	) {
		FeedEntity feedEntity = bookMarkEntity.getFeedEntity();
		ScheduleEntity scheduleEntity = feedEntity.getSchedule();
		MemberEntity creatorMemberEntity = feedEntity.getCreatorMember();

		return new GetBookMarkResponseDto(
			creatorMemberEntity.getMemberSeq(),
			bookMarkEntity.getMemberEntity().getProfileImage(),
			scheduleEntity.getStartTime(),
			scheduleEntity.getLocation(),
			feedEntity.getTitle(),
			toBookMarkImageInfoList(feedImageEntities),
			toBookMarkFriendInfoList(memberEntities),
			feedEntity.getContent(),
			true
		);
	}

	default List<BookMarkImageInfo> toBookMarkImageInfoList(List<FeedImageEntity> feedImageEntities) {
		return feedImageEntities.stream()
			.map(this::toBookMarkImageInfo)
			.toList();
	}

	default BookMarkImageInfo toBookMarkImageInfo(FeedImageEntity feedImageEntity) {
		return new BookMarkImageInfo(
			feedImageEntity.getFeedImageSeq(),
			feedImageEntity.getFeedImageURL(),
			feedImageEntity.getFeedImageOrder()
		);
	}

	default List<BookMarkFriendInfo> toBookMarkFriendInfoList(List<MemberEntity> memberEntities) {
		return memberEntities.stream()
				.map(this::toBookMarkFriendInfoList)
				.toList();
	}

	default BookMarkFriendInfo toBookMarkFriendInfoList(MemberEntity memberEntity) {
		return new BookMarkFriendInfo(
				memberEntity.getMemberSeq(),
				memberEntity.getProfileImage()
		);
	}

}
