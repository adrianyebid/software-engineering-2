package com.gymapp.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(public * com.gymapp.service..*(..))")
    private void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info(">> {}() - args={}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "serviceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        logger.info("<< {}() - result={}", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(value = "serviceMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        logger.error("!! {}() - exception={}", joinPoint.getSignature().getName(), exception.getMessage());
    }
}
