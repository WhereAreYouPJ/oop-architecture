package way.application.service.member.dto.response;

public class MemberResponseDto {

    public record CheckIdResponseDto(

            String userId
    ) {

    }

    public record CheckEmailResponseDto(

            String email
    ) {

    }

    public record LoginResponseDto(
            String accessToken,
            String refreshToken,
            Long memberSeq
    ) {

    }
}
