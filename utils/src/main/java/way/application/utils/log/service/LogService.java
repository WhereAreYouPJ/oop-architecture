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
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogJpaRepository logJpaRepository;

    @EventListener
    public void handleLogEvent(LogEvent event) {
        LogEntity logEntity = LogEntity.builder()
                .httpStatus(event.getHttpStatus())
                .requestUri(event.getRequestUri())
                .message(event.getMessage())
                .errorCode(event.getErrorCode())
                .exception(event.getException())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .param(event.getParam())
                .build();

        logJpaRepository.save(logEntity);
    }

    public List<LogEntity> searchLogs(Integer level, String startDate, String endDate) {
        return logJpaRepository.searchLogs(level, startDate, endDate + " 23:59:59");
    }
}
