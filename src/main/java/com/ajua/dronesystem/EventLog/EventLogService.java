package com.ajua.dronesystem.EventLog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventLogService {
    private final EventLogRepository eventLogRepository;

    public void createAudit(String event, String user){
        EventLog eventLog = new EventLog();
        eventLog.setEvent(event);
        eventLog.setUsername(user);
        eventLogRepository.save(eventLog);
    }
}
