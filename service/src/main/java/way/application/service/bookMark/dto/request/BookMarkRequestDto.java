package way.application.service.bookMark.dto.request;

public class BookMarkRequestDto {
	public record AddBookMarkRequestDto(
		Long feedSeq,
		Long memberSeq
	) {

	}

	public record DeleteBookMarkRequestDto(
		Long feedSeq,
		Long memberSeq
	) {

	}
}
