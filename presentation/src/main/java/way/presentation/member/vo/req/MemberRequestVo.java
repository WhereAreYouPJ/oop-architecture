package way.presentation.member.vo.req;

import org.springframework.web.multipart.MultipartFile;
import way.application.service.member.dto.request.MemberRequestDto;

import static way.application.service.member.dto.request.MemberRequestDto.*;

public class MemberRequestVo {

    public record SaveMemberRequest(
            String userName,

            String password,

            String email
    ) {
        public SaveMemberRequestDto toSaveMemberRequestDto() {
            return new SaveMemberRequestDto(
                    this.userName,
                    this.password,
                    this.email
            );
        }
    }

    public record SaveSnsMemberRequest(
            String userName,
            String email,
            String password,
            String loginType,
            String fcmToken

    ) {
        public SaveSnsMemberRequestDto toSaveSnsMemberRequestDto() {
            return new SaveSnsMemberRequestDto(
                    this.userName,
                    this.password,
                    this.email,
                    this.loginType,
                    this.fcmToken
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
            String password,
            String fcmToken,
            String loginType
    ) {
        public MemberRequestDto.LoginRequestDto toLoginRequestDto() {
            return new MemberRequestDto.LoginRequestDto(
                    this.email,
                    this.password,
                    this.fcmToken,
                    this.loginType
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
            return new MemberRequestDto.PasswordResetRequestDto(
                    this.email,
                    this.password,
                    this.checkPassword
            );
        }

    }

    public record GetMemberDetailRequest(
            Long memberSeq
    ) {

        public MemberRequestDto.GetMemberDetailDto toGetMemberDetailDto() {
            return new MemberRequestDto.GetMemberDetailDto(
                    this.memberSeq
            );
        }
    }

    public record SearchMember(
            String memberCode
    ) {

        public MemberRequestDto.SearchMemberDto toSearchMemberDto() {
            return new MemberRequestDto.SearchMemberDto(
                    this.memberCode
            );
        }
    }

    public record ModifyProfileImage(
            Long memberSeq,
            MultipartFile multipartFile
    ) {
        public MemberRequestDto.ModifyProfileImage toModifyProfileImage() {
            return new MemberRequestDto.ModifyProfileImage(
                    this.memberSeq,
                    this.multipartFile
            );
        }
    }

    public record LogoutRequest(

            Long memberSeq
    ) {
        public MemberRequestDto.LogoutRequestDto toLogoutRequest() {
            return new MemberRequestDto.LogoutRequestDto(
                    this.memberSeq
            );
        }
    }

    public record ModifyUserNameRequest(
            Long memberSeq,
            String userName
    ) {
        public MemberRequestDto.ModifyUserNameDto toModifyUserNameRequest() {
            return new MemberRequestDto.ModifyUserNameDto(
                    this.memberSeq,
                    this.userName
            );
        }
    }

    public record DeleteMemberRequest(
            Long memberSeq,
            String password,
            String comment,
            String loginType
    ) {
        public MemberRequestDto.DeleteMemberDto toDeleteMemberDtoRequest() {
            return new MemberRequestDto.DeleteMemberDto(
                    this.memberSeq,
                    this.password,
                    this.comment,
                    this.loginType
            );
        }
    }

    public record TokenReissueRequest(
            String refreshToken
    ) {
        public MemberRequestDto.TokenReissueRequestDto toTokenReissueRequest() {
            return new MemberRequestDto.TokenReissueRequestDto(
                    this.refreshToken
            );
        }
    }

}
