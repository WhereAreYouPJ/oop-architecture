package way.presentation.Member.vo.res;

public class MemberResponseVo {

    public record CheckIdResponse(
            String userId

    ) {

    }

    public record CheckEmailResponse(
            String email

    ) {

    }
}