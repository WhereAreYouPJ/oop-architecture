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
}
