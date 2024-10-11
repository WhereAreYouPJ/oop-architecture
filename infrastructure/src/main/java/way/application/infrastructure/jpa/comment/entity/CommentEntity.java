package way.application.infrastructure.jpa.comment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "COMMENT")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class CommentEntity {


    @Id
    @Column(name = "comment_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentSeq;

    private String comment;

}
