package way.application.service.bookMark.dto.request;

public class BookMarkRequestDto {
	public record AddBookMarkRequestDto(
		Long scheduleSeq,
		Long memberSeq
	) {

	}

	public record DeleteBookMarkRequestDto(
		Long bookMarkScheduleSeq,
		Long memberSeq
	) {

	}
}
