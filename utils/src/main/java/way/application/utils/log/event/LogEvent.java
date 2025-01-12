package way.application.utils.log.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LogEvent {

    private final int httpStatus;
    private final String message;
    private final String errorCode;
    private final String exception;
}
