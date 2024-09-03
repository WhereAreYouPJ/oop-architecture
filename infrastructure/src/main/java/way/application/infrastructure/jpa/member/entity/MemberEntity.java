package way.application.infrastructure.jpa.member.entity;

import org.hibernate.annotations.ColumnDefault;
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
@Table(name = "MEMBER")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class MemberEntity {
	@Id
	@Column(name = "member_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberSeq;

	@Column(nullable = false)
	private String userName;

	@Column(nullable = false)
	private String encodedPassword;

	private String email;

	private String profileImage;

	private String fireBaseTargetToken;

	@ColumnDefault("'false'")
	private String apple;

	@ColumnDefault("'false'")
	private String kakao;

	@ColumnDefault("'false'")
	private String normal;

	private String memberCode;

	public void updateEncodedPassword(String password) {
		this.encodedPassword = password;
	}

	public void updateProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void saveFireBaseTargetToken(String fireBaseTargetToken) {
		this.fireBaseTargetToken = fireBaseTargetToken;
	}

	public void deleteFireBaseTargetToken() {
		this.fireBaseTargetToken = null;
	}
}
