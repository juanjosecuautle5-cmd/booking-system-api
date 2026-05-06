package com.juan.dev.bookingsystem.repository;

import com.juan.dev.bookingsystem.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}