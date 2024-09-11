package com.aliens.backend.global.aspect.log.board.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class MarketPointcut {

    @Pointcut("execution(* com.aliens.backend.board.controller.MarketController.createMarketBoard(..))")
    public void createMarketBoard() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.MarketController.getMarketBoardPage(..))")
    public void getMarketBoardPage() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.MarketController.getMarketBoardDetails(..))")
    public void getMarketBoardDetails() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.MarketController.searchMarketBoards(..))")
    public void searchMarketBoards() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.MarketController.changeMarketBoard(..))")
    public void changeMarketBoard() {}

}
