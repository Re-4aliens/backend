package com.aliens.backend.global.aspect.log.board.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class GreatPointcut {

    @Pointcut("execution(* com.aliens.backend.board.controller.GreatController.greatAtBoard(..))")
    public void greatAtBoard() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.GreatController.getAllGreatBoards(..))")
    public void getAllGreatBoards() {}
}
