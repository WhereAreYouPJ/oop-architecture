package way.presentation.member.vo.res;

import java.util.List;

public class MemberResponseVo {

    public record CheckIdResponse(
            String userId

    ) {

    }

    public record CheckEmailResponse(
            String email,

            List<String> type

    ) {

    }

    public record GetMemberDetailResponse(
            String userName,
            String email,
            String profileImage
    ) {

    }

    public record SearchMemberResponse(
            String userName,
            Long memberSeq,
            String profileImage
    ) {

    }

    public record TokenReissueResponse(
            String accessToken,
            String refreshToken,
            Long memberSeq
    ) {

    }

}
