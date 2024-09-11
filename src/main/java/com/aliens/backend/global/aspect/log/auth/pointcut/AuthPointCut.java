package com.aliens.backend.global.aspect.log.auth.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class AuthPointCut {
    @Pointcut("execution(* com.aliens.backend.auth.controller.AuthController.login(..))")
    public void login() {}

    @Pointcut("execution(* com.aliens.backend.auth.controller.AuthController.reissue(..))")
    public void reissue() {}

    @Pointcut("execution(* com.aliens.backend.auth.controller.AuthController.logout(..))")
    public void logout() {}
}
