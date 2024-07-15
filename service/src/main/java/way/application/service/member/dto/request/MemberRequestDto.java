package way.application.service.member.dto.request;

public class MemberRequestDto {

    public record SaveMemberRequestDto (
            String userName,

            String userId,

            String password,

            String email
    ) {

    }
}
