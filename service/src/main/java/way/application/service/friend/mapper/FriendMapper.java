package way.application.service.friend.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import way.application.infrastructure.jpa.friend.entity.FriendEntity;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FriendMapper {
    @Mapping(target = "friendSeq", ignore = true)
    @Mapping(target = "owner", source = "member")
    @Mapping(target = "friends", source = "sender")
    FriendEntity toFriendEntity(MemberEntity member, MemberEntity sender);

}
