package way.application.service.member.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class MemberRequestDto {

    public record SaveMemberRequestDto (
            String userName,
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

    public record PasswordResetRequestDto(
            String email,
            String password,
            String checkPassword
    ) {
    }

    public record GetMemberDetailDto(
            Long memberSeq
    ) {
    }

    public record ModifyProfileImage(
            Long memberSeq,
            MultipartFile multipartFile
    ) {
    }

    public record LogoutRequestDto(
            Long memberSeq
    ) {
    }

    public record SearchMemberDto(

            String memberCode
    ) {

    }
}
