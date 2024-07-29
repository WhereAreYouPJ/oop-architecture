package way.application.service.friendRequest.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import way.application.infrastructure.friendRequest.entity.FriendRequestEntity;
import way.application.infrastructure.member.entity.MemberEntity;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FriendRequestMapper {

    @Mapping(target = "friendRequestSeq", ignore = true)
    @Mapping(target = "senderSeq", source = "memberSeq")
    @Mapping(target = "receiverSeq", source = "friendSeq")
    @Mapping(target = "createTime", source = "now")
    FriendRequestEntity toFriendRequestEntity(MemberEntity memberSeq, MemberEntity friendSeq, LocalDateTime now);


}
