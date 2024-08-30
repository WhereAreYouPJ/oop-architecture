package way.application.service.feed.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class FeedResponseDto {
	public record SaveFeedResponseDto(
		Long feedSeq
	) {

	}

	public record ModifyFeedResponseDto(
		Long feedSeq
	) {

	}

	public record GetAllFeedResponseDto(
		ScheduleInfo scheduleInfo,
		List<ScheduleFeedInfo> scheduleFeedInfo
	) {
		public record ScheduleFeedInfo(
			MemberInfo memberInfo,
			FeedInfo feedInfo,
			List<FeedImageInfo> feedImageInfos,
			boolean bookMarkInfo
		) {

		}

		public record MemberInfo(
			Long memberSeq,
			String userName,
			String profileImage
		) {

		}

		public record ScheduleInfo(
			Long scheduleSeq,
			LocalDateTime startTime,
			String location
		) {

		}

		public record FeedInfo(
			Long feedSeq,
			String title,
			String content
		) {

		}

		public record FeedImageInfo(
			Long feedImageSeq,
			String feedImageURL,
			Long feedImageOrder
		) {

		}
	}
}
