package way.application.infrastructure.jpa.schedule.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SCHEDULE")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
public class ScheduleEntity {
	@Id
	@Column(name = "schedule_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long scheduleSeq;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;

	@Column(name = "end_time", nullable = false)
	private LocalDateTime endTime;

	@Column(name = "location", nullable = true)
	private String location;

	@Column(name = "street_name", nullable = true)
	private String streetName;

	@Column(name = "x", nullable = true)
	private Double x;

	@Column(name = "y", nullable = true)
	private Double y;

	@Column(name = "color", nullable = false, columnDefinition = "varchar(255) default 'Color_7B50FF'")
	private String color;

	@Lob
	@Column(name = "memo", nullable = true)
	private String memo;

	@Column(name = "all_day", columnDefinition = "boolean default false")
	private Boolean allDay;

	public ScheduleEntity updateScheduleEntity(
		String title,
		LocalDateTime startTime,
		LocalDateTime endTime,
		String location,
		String streetName,
		Double x,
		Double y,
		String color,
		String memo,
		Boolean allDay
	) {
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.location = location;
		this.streetName = streetName;
		this.x = x;
		this.y = y;
		this.color = color;
		this.memo = memo;
		this.allDay = allDay;

		return this;
	}
}
