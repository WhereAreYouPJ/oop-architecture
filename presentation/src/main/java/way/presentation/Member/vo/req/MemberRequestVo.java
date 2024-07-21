package way.presentation.Member.vo.req;

import way.application.service.member.dto.request.MemberRequestDto;

import static way.application.service.member.dto.request.MemberRequestDto.*;

public class MemberRequestVo {

    public record SaveMemberRequest(
            String userName,

            String userId,

            String password,

            String email
    ) {
        public SaveMemberRequestDto toSaveMemberRequestDto() {
            return new SaveMemberRequestDto(
                    this.userName,
                    this.userId,
                    this.password,
                    this.email
            );
        }
    }

    public record CheckIdRequest(
            String userId
    ) {
        public MemberRequestDto.CheckIdRequestDto toCheckIdRequestDto() {
            return new MemberRequestDto.CheckIdRequestDto(
                    this.userId
            );
        }
    }

    public record CheckEmailRequest(

            String email
    ) {
        public MemberRequestDto.CheckEmailRequestDto toCheckEmailRequestDto() {
            return new MemberRequestDto.CheckEmailRequestDto(
                    this.email
            );
        }
    }

    public record LoginRequest(

            String email,
            String password
    ) {
        public MemberRequestDto.LoginRequestDto toLoginRequestDto() {
            return new MemberRequestDto.LoginRequestDto(
                    this.email,
                    this.password
            );
        }
    }

    public record MailSendRequest(

            String email

    ) {
        public MemberRequestDto.MailSendRequestDto toMailSendRequestDto() {
            return new MemberRequestDto.MailSendRequestDto(
                    this.email
            );
        }
    }

    public record VerifyCodeRequest(
            String email,
            String code
    ) {

        public MemberRequestDto.VerifyCodeDto toVerifyCodeDto() {
            return new MemberRequestDto.VerifyCodeDto(
                    this.email,
                    this.code
            );
        }

    }

    public record PasswordResetRequest(

            String email,
            String password,
            String checkPassword
    ) {
        public MemberRequestDto.PasswordResetRequestDto toPasswordResetRequestDto() {
            return new MemberRequestDto.PasswordResetRequestDto(this.email,
                    this.password,
                    this.checkPassword
            );
        }

    }
}
