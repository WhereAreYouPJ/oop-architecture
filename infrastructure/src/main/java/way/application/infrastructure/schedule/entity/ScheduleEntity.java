package way.application.infrastructure.schedule.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	private String title;

	private LocalDateTime startTime;
	private LocalDateTime endTime;

	private String location;
	private String streetName;

	private Double x;
	private Double y;

	private String color;

	@Column(length = 5000)
	private String memo;
}
