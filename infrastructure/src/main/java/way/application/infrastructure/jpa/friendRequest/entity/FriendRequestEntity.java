package way.application.infrastructure.jpa.friendRequest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import way.application.infrastructure.jpa.member.entity.MemberEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "FRIEND_REQUEST")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class FriendRequestEntity {

    @Id
    @Column(name = "friendRequest_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendRequestSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_seq")
    private MemberEntity senderSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_seq")
    private MemberEntity receiverSeq;

    private LocalDateTime createTime;
}