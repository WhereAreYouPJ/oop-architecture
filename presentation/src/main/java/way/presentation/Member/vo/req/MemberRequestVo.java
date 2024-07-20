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

}
