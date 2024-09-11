package com.aliens.backend.global.aspect.log.board.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class BoardPointcut {

    @Pointcut("execution(* com.aliens.backend.board.controller.BoardController.createBoard(..))")
    public void createBoard() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.BoardController.getSingleBoard(..))")
    public void getSingleBoard() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.BoardController.getAllBoards(..))")
    public void getAllBoards() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.BoardController.getAllBoardsWithCategory(..))")
    public void getAllBoardsWithCategory() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.BoardController.searchAllBoardPage(..))")
    public void searchAllBoardPage() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.BoardController.getAllAnnouncementBoards(..))")
    public void getAllAnnouncementBoards() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.BoardController.getPageMyBoards(..))")
    public void getPageMyBoards() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.BoardController.searchBoardsWithCategory(..))")
    public void searchBoardsWithCategory() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.BoardController.deleteBoard(..))")
    public void deleteBoard() {}
}
