package com.juan.dev.bookingsystem.aspect;

import com.juan.dev.bookingsystem.annotation.Auditable;
import com.juan.dev.bookingsystem.service.AuditService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @AfterReturning("@annotation(auditable)")
    public void logAction(JoinPoint joinPoint,
                          Auditable auditable) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        auditService.log(email, auditable.value());
    }
}