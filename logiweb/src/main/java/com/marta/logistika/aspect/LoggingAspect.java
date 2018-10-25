package com.marta.logistika.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LoggingAspect {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Around("execution(* com.marta.logistika.service..*.*(..)) || execution(* com.marta.logistika.controller..*.*(..))")
	public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

	    Object returnedValue = joinPoint.proceed();

        StringBuilder logMessage = new StringBuilder("###LOGIWEB### ");
        logMessage.append(joinPoint.getTarget().getClass().getName());
        logMessage.append(".");
        logMessage.append(joinPoint.getSignature().getName());

        logMessage.append("(");
        logMessage.append(Arrays.toString(joinPoint.getArgs()));
        logMessage.append(")");

        logMessage.append(" returned ");
        logMessage.append(returnedValue);

        LOGGER.debug(logMessage.toString());
        return returnedValue;
	}

    @AfterThrowing(
            pointcut = "execution(* com.marta.logistika.service..*.*(..)) || execution(* com.marta.logistika.controller..*.*(..))",
            throwing= "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {

        StringBuilder logMessage = new StringBuilder("###LOGIWEB### ");
        logMessage.append(joinPoint.getTarget().getClass().getName());
        logMessage.append(".");
        logMessage.append(joinPoint.getSignature().getName());
        logMessage.append(" Exception : ");
        logMessage.append(error);

        LOGGER.error(logMessage.toString(), error);
    }
	
}