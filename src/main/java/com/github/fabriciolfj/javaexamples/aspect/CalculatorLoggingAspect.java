package com.github.fabriciolfj.javaexamples.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.hibernate.mapping.Join;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class CalculatorLoggingAspect {

    @Before("execution(* com.github.fabriciolfj.javaexamples.service.impl.StandardArithmeticCalculator.add(..))")
    public void logBefore() {
        log.info("the method add() begins");
    }

    @Before("execution(* com.github.fabriciolfj.javaexamples.service.impl.StandardArithmeticCalculator.*(..))")
    public void logBefore(final JoinPoint joinpoint) {
        var name = joinpoint.getSignature().getName();
        var args = Arrays.asList(joinpoint.getArgs());

        log.info("the method {} () begins with {} ", name, args);
    }

    @After("execution(* com.github.fabriciolfj.javaexamples.service.impl.StandardArithmeticCalculator.*(..))")
    public void logAfter(final JoinPoint joinPoint) {
        var name = joinPoint.getSignature().getName();
        log.info("the method {} () end", name);
    }

   @AfterReturning(pointcut = "execution(* com.github.fabriciolfj.javaexamples.service.impl.StandardArithmeticCalculator.*(..))", returning = "result")
    public void logAfter(final JoinPoint joinPoint, final Object result) {
        var name = joinPoint.getSignature().getName();
        log.info("the method {} () end with {}", name, result);
    }

    @AfterThrowing(pointcut = "execution(* com.github.fabriciolfj.javaexamples.service.impl.StandardArithmeticCalculator.*(..))",
        throwing = "t")
    public void logAfter(final JoinPoint joinPoint, final Throwable t) {
        var name = joinPoint.getSignature().getName();
        log.info("args invalid method {}, exception {}", name, t.getMessage());
    }

    @Around("execution(* com.github.fabriciolfj.javaexamples.service.impl.StandardArithmeticCalculator.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        var name = joinPoint.getSignature().getName();
        var args = Arrays.toString(joinPoint.getArgs());
        log.info("The logAround {}() begins with {}.", name, args);
        try {
            var result = joinPoint.proceed();
            log.info("The logAround {}() ends with {}.", name, result);
            return result;
        }
        catch (IllegalArgumentException ex) {
            log.error("logAround Illegal argument {} in {}()", args, name);
            throw ex;
        }
    }
}
