package way.application.service.bookMark.dto.request;

public class BookMarkRequestDto {
	public record AddBookMarkRequestDto(
		Long bookMarkFeedSeq,
		Long memberSeq
	) {

	}
}
