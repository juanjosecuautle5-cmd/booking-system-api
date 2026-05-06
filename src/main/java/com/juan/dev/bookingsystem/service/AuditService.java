package com.juan.dev.bookingsystem.service;

import com.juan.dev.bookingsystem.model.AuditLog;
import com.juan.dev.bookingsystem.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String userEmail, String action) {

        AuditLog log = new AuditLog();

        log.setUserEmail(userEmail);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }
}