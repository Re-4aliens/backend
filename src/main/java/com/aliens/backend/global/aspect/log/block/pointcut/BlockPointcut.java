package com.aliens.backend.global.aspect.log.block.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class BlockPointcut {

    @Pointcut("execution(* com.aliens.backend.block.controller.BlockController.blockPartner(..))")
    public void blockPartner() {}
}
