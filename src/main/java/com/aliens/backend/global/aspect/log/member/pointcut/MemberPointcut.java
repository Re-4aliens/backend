package com.aliens.backend.global.aspect.log.member.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class MemberPointcut {
    @Pointcut("execution(* com.aliens.backend.member.controller.MemberController.signUp(..))")
    public void signUp() {}

    @Pointcut("execution(* com.aliens.backend.member.controller.MemberController.temporaryPassword(..))")
    public void temporaryPassword() {}

    @Pointcut("execution(* com.aliens.backend.member.controller.MemberController.changePassword(..))")
    public void changePassword() {}

    @Pointcut("execution(* com.aliens.backend.member.controller.MemberController.changeProfileImage(..))")
    public void changeProfileImage() {}

    @Pointcut("execution(* com.aliens.backend.member.controller.MemberController.changeAboutMe(..))")
    public void changeAboutMe() {}

    @Pointcut("execution(* com.aliens.backend.member.controller.MemberController.changeMBTI(..))")
    public void changeMBTI() {}

    @Pointcut("execution(* com.aliens.backend.member.controller.MemberController.withdraw(..))")
    public void withdraw() {}

    @Pointcut("execution(* com.aliens.backend.member.controller.MemberController.getStatus(..))")
    public void getStatus() {}

    @Pointcut("execution(* com.aliens.backend.member.controller.MemberController.getMemberPage(..))")
    public void getMemberPage() {}
}
