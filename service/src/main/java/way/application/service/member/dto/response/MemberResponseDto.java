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
            Long memberSeq,
            String memberCode
    ) {

    }

    public record GetMemberDetailResponseDto(
            String userName,
            String email,
            String profileImage
    ) {

    }

    public record SearchMemberResponseDto(
            String userName,
            Long memberSeq,
            String profileImage
    ) {

    }

}
