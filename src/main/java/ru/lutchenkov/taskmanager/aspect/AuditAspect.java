package ru.lutchenkov.taskmanager.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AuditAspect {

    @Before("@annotation(ru.lutchenkov.taskmanager.annotation.Auditable)")
    public void auditMethod(JoinPoint joinPoint) {
        String user = "system";
        String methodName = joinPoint.getSignature().getName();
        LocalDateTime time = LocalDateTime.now();

        log.info("🛡️ [AUDIT] Пользователь: '{}' | Метод: '{}' | Время: {}", user, methodName, time);
    }
}