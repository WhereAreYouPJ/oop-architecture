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

    @Column(name = "http_status", nullable = false)
    private int httpStatus;

    @Lob
    @Column(name = "message", nullable = false, columnDefinition = "LONGTEXT")
    private String message;

    @Column(name = "error_code", nullable = false)
    private String errorCode;

    @Column(name = "exception", nullable = false)
    private String exception;

    @Column(name = "timestamp", nullable = false)
    private String timestamp;

    @Column(name = "request_uri", nullable = false)
    private String requestUri;

    @Column(name = "param", nullable = false)
    private String param;
}

