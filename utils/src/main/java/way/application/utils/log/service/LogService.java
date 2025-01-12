package way.application.utils.log.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import way.application.utils.log.entity.LogEntity;
import way.application.utils.log.event.LogEvent;
import way.application.utils.log.repository.LogJpaRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogJpaRepository logJpaRepository;

    @EventListener
    public void handleLogEvent(LogEvent event) {
        LogEntity logEntity = LogEntity.builder()
                .httpStatus(event.getHttpStatus())
                .message(event.getMessage())
                .errorCode(event.getErrorCode())
                .exception(event.getException())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        logJpaRepository.save(logEntity);
    }

    public Page<LogEntity> searchLogs(Integer level, String startDate, String endDate, Pageable pageable) {
        Page<LogEntity> logEntities = logJpaRepository.searchLogs(level, startDate, endDate, pageable);
        return logEntities;
    }
}
