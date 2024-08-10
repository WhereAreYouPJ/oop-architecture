package way.presentation.feed.vo.req;

import static way.application.service.feed.dto.request.FeedRequestDto.*;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

public class FeedRequestVo {
	public record SaveFeedRequest(
		@Schema(description = "Feed에 해당하는 Schedule Seq")
		Long scheduleSeq,

		@Schema(description = "Feed 생성자 Member Seq")
		Long creatorSeq,

		@Schema(description = "제목")
		String title,

		@Schema(description = "내용")
		String content,

		@Schema(description = "이미지")
		List<MultipartFile> images
	) {
		public SaveFeedRequestDto toSaveFeedRequest() {
			return new SaveFeedRequestDto(
				this.scheduleSeq,
				this.creatorSeq,
				this.title,
				this.content,
				this.images
			);
		}
	}

	public record ModifyReedRequest(
		@Schema(description = "수정하고자 하는 Feed Seq")
		Long feedSeq,

		@Schema(description = "Feed 생성자 Member Seq")
		Long creatorSeq,

		@Schema(description = "제목")
		String title,

		@Schema(description = "내용")
		String content,

		@Schema(description = "이미지")
		List<MultipartFile> images
	) {
		public ModifyFeedRequestDto toModifyFeedRequestDto() {
			return new ModifyFeedRequestDto(
				this.feedSeq,
				this.creatorSeq,
				this.title,
				this.content,
				this.images
			);
		}
	}
}
