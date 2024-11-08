package way.application.infrastructure.jpa.coordinate.entity;

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
import way.application.infrastructure.jpa.member.entity.MemberEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

@Entity
@Table(name = "COORDINATE")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class CoordinateEntity {
	@Id
	@Column(name = "coordinate_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long coordinateSeq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_seq")
	private MemberEntity memberEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_seq")
	private ScheduleEntity scheduleEntity;

	@Column(name = "x", nullable = false)
	private double x;

	@Column(name = "y", nullable = false)
	private double y;

	public void updateCoordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
