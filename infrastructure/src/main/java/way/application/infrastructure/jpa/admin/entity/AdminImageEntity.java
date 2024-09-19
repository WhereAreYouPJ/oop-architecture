package way.application.infrastructure.jpa.admin.entity;

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
@Table(name = "ADMIN_IMAGE")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class AdminImageEntity {
	@Id
	@Column(name = "admin_image_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long adminImageSeq;

	@Column(name = "image_url", nullable = false)
	public String imageURL;
}
