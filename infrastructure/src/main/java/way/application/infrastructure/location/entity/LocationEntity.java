package way.application.infrastructure.location.entity;

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
import way.application.infrastructure.member.entity.MemberEntity;

@Entity
@Table(name = "LOCATION")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class LocationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "location_seq")
	private Long locationSeq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_seq")
	private MemberEntity memberEntity;

	@Column(name = "location", nullable = false)
	private String location;

	@Column(name = "street_name", nullable = false)
	private String streetName;
}
