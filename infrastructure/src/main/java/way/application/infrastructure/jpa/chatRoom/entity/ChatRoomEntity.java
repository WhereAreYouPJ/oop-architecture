package way.application.infrastructure.jpa.chatRoom.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import way.application.infrastructure.base.entity.BaseEntity;
import way.application.infrastructure.jpa.schedule.entity.ScheduleEntity;

@Entity
@Table(name = "CHAT_ROOM")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class ChatRoomEntity extends BaseEntity {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "chat_room_seq", updatable = false, nullable = false)
	private String chatRoomSeq;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_seq")
	private ScheduleEntity scheduleEntity;
}
