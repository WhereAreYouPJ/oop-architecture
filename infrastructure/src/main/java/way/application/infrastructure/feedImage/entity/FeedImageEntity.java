package way.application.infrastructure.feedImage.entity;

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

@Entity
@Table(name = "FEED_IMAGE")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
public class FeedImageEntity {
	@Id
	@Column(name = "feed_image_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long feedImageSeq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feed_seq")
	private FeedEntity feedEntity;

	private String feedImageURL;
}
