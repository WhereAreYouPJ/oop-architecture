package way.application.infrastructure.scheduleMember.entity;

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
import lombok.RequiredArgsConstructor;
import way.application.infrastructure.member.entity.MemberEntity;
import way.application.infrastructure.schedule.entity.ScheduleEntity;

@Entity
@Table(name = "SCHEDULE_MEMBER")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
public class ScheduleMemberEntity {
	@Id
	@Column(name = "schedule_member_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long scheduleMemberSeq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_seq")
	private ScheduleEntity schedule;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_seq")
	private MemberEntity invitedMember;

	@Column(name = "is_creator", nullable = false)
	private Boolean isCreator = false;

	@Column(name = "accept_schedule", nullable = false)
	private Boolean acceptSchedule = false;

	public void updateAcceptSchedule() {
		this.acceptSchedule = true;
	}
}
