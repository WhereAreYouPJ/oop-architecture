package way.application.utils.log.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Table(name = "LOG")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class LogEntity {

    @Id
    @Column(name = "log_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logSeq;

    private int httpStatus;
    private String message;
    private String errorCode;
    private String exception;
    private String timestamp;
    private String requestUri;
    private String param;
}

