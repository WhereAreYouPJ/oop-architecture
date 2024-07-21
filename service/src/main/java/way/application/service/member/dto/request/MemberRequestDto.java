package way.application.service.member.dto.request;

public class MemberRequestDto {

    public record SaveMemberRequestDto (
            String userName,

            String userId,

            String password,

            String email
    ) {

    }

    public record CheckIdRequestDto (
            String userId

    ) {

    }

    public record CheckEmailRequestDto (
            String email

    ) {

    }

    public record LoginRequestDto (
            String email,
            String password
    ) {

    }

    public record MailSendRequestDto(
            String email
    ) {
    }

    public record VerifyCodeDto(
            String email,
            String code

    ) {
    }
}
