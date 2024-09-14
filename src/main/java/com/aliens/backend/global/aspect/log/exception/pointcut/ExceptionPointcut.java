package com.aliens.backend.global.aspect.log.exception.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class ExceptionPointcut {

    @Pointcut("execution(* com.aliens.backend.global.exception.ApiExceptionHandler.apiException(..))")
    public void exceptionHandler() {}
}
