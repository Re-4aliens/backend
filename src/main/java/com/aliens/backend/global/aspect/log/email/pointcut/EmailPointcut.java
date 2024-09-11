package com.aliens.backend.global.aspect.log.email.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class EmailPointcut {
    @Pointcut("execution(* com.aliens.backend.email.controller.EmailController.duplicateCheck(..))")
    public void duplicateCheck() {}

    @Pointcut("execution(* com.aliens.backend.email.controller.EmailController.sendAuthenticationEmail(..))")
    public void sendAuthenticationEmail() {}

    @Pointcut("execution(* com.aliens.backend.email.controller.EmailController.authenticateEmail(..))")
    public void authenticateEmail() {}

    @Pointcut("execution(* com.aliens.backend.email.controller.EmailController.checkEmailAuthenticated(..))")
    public void checkEmailAuthenticated() {}
}
