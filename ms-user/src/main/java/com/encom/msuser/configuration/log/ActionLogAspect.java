package com.encom.msuser.configuration.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Configuration
@Slf4j
public class ActionLogAspect {
    //  Controller layer
    @Before("execution(* com.encom.msuser.controller.*.*(..))" +
            "&& !execution(* com.encom.msuser.exception.ExceptionHook.*(..))")
    public void beforeController(JoinPoint joinPoint) {
        Map<String, Object> parameters = getParameters(joinPoint);

        log.info("ActionLog.controller.{}.start.withParams.{}", joinPoint.getSignature().getName(), parameters);
    }

    //  Controller layer
    @AfterReturning(value = "execution(* com.encom.msuser.controller.*.*(..))" +
            "&& !execution(* com.encom.msuser.exception.ExceptionHook.*(..))", returning = "result")
    public void afterController(JoinPoint joinPoint, Object result) {
        log.info("ActionLog.controller.{}.success.withResult.{}", joinPoint.getSignature().getName(), result);
    }

    //  Service layer
    @Before("execution(* com.encom.msuser.service.*.*(..))" +
            "&& !execution(* com.encom.msuser.exception.ExceptionHook.*(..))")
    public void beforeService(JoinPoint joinPoint) {
        Map<String, Object> parameters = getParameters(joinPoint);

        log.info("ActionLog.service.{}.start.withParams.{}", joinPoint.getSignature().getName(), parameters);
    }

    //  Service layer
    @AfterReturning(value = "execution(* com.encom.msuser.service.*.*(..))" +
            "&& !execution(* com.encom.msuser.exception.ExceptionHook.*(..))", returning = "result")
    public void afterService(JoinPoint joinPoint, Object result) {
        log.info("ActionLog.service.{}.success.withResult.{}", joinPoint.getSignature().getName(), result);
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        HashMap<String, Object> map = new HashMap<>();

        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }

        return map;
    }
}
