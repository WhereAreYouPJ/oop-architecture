package way.application.service.member.dto.response;

import java.util.List;

public class MemberResponseDto {

    public record CheckIdResponseDto(

            String userId
    ) {

    }

    public record CheckEmailResponseDto(

            String email,
            List<String> loginTypeList
    ) {

    }

    public record LoginResponseDto(
            String accessToken,
            String refreshToken,
            Long memberSeq,
            String memberCode,
            String profileImage
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

    public record TokenReissueResponseDto(
            String accessToken,
            String refreshToken,
            Long memberSeq
    ) {

    }

}
