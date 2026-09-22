package ru.lutchenkov.taskmanager.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.lutchenkov.taskmanager.annotation.RateLimit;
import ru.lutchenkov.taskmanager.exception.TooManyRequestsException;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Aspect
@Component
public class RateLimitAspect {
    private final Map<String, Queue<Long>> requestHistory = new ConcurrentHashMap<>();

    @Before("@annotation(rateLimitAnnotation)")
    public void checkRateLimit(JoinPoint joinPoint, RateLimit rateLimitAnnotation) {
        int limit = rateLimitAnnotation.requestsPerMinute();
        String key = rateLimitAnnotation.key();

        if (key.isEmpty()) {
            key = joinPoint.getSignature().getName();
        }

        Queue<Long> queue = requestHistory.computeIfAbsent(key, k -> new ConcurrentLinkedQueue<>());

        long currentTime = System.currentTimeMillis();
        long oneMinuteAgo = currentTime - 60_000;

        while (!queue.isEmpty() && queue.peek() < oneMinuteAgo) {
            queue.poll();
        }

        if (queue.size() >= limit) {
            throw new TooManyRequestsException("Превышен лимит запросов. Попробуйте позже");
        }

        queue.add(currentTime);
    }
}