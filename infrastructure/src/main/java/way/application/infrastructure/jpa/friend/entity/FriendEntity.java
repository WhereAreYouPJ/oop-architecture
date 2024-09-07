package way.application.infrastructure.jpa.friend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import way.application.infrastructure.jpa.member.entity.MemberEntity;

@Entity
@Table(name = "FRIEND")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class FriendEntity {

    @Id
    @Column(name = "friend_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friends_seq")
    private MemberEntity friends;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_seq")
    private MemberEntity owner;

    @Column(columnDefinition = "boolean default false")
    private boolean Favorites;


    public void addFavorites() {
        Favorites = true;
    }

    public void removeFavorites() {
        Favorites = false;
    }
}
