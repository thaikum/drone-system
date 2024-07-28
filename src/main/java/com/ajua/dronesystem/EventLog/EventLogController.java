package com.ajua.dronesystem.EventLog;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("event-log")
@RequiredArgsConstructor
public class EventLogController {
    private final EventLogRepository eventLogRepository;

    @GetMapping
    public ResponseEntity<List<EventLog>> getAll() {
        return ResponseEntity.ok(eventLogRepository.findAll());
    }
}
