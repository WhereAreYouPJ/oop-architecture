package way.presentation.Member.vo.req;

import way.application.service.member.dto.request.MemberRequestDto;
import way.application.service.schedule.dto.request.ScheduleRequestDto;

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
}
