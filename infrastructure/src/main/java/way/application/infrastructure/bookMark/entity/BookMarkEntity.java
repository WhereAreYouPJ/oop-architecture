package way.application.infrastructure.bookMark.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import way.application.infrastructure.feed.entity.FeedEntity;
import way.application.infrastructure.member.entity.MemberEntity;

@Entity
@Table(name = "BOOK_MARK")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class BookMarkEntity {
	@Id
	@Column(name = "book_mark_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookMarkSeq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feed_seq")
	private FeedEntity feedEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_seq")
	private MemberEntity memberEntity;
}
