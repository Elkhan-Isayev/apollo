package com.encom.msuser.configuration.aspect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

@Aspect
@Configuration
@Slf4j
public class ExecutionTimeAspect {
    @Around("@annotation(com.encom.msuser.configuration.annotation.LogExecutionTime)")
    @SneakyThrows
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        Object proceed = joinPoint.proceed();

        stopWatch.stop();

        log.info("ExecuteTimeLog.{}.executed.{} ms", joinPoint.getSignature(), stopWatch.getTotalTimeMillis());

        return proceed;
    }
}
