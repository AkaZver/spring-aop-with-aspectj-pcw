package com.github.akazver.poc.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Log4j2
@Aspect
public class AnnotationAspect {

    @Around("@annotation(com.github.akazver.poc.annotation.Count) && execution(* com.github.akazver.poc..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();

        try {
            return joinPoint.proceed();
        } catch (Exception exception) {
            log.error("Exception occurred", exception);
            throw exception;
        } finally {
            log.info("Executed in {}ns", System.nanoTime() - start);
        }
    }

}
