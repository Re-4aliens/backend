package com.aliens.backend.global.aspect.log.board.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class CommentPointcut {
    @Pointcut("execution(* com.aliens.backend.board.controller.CommentController.createParentComment(..))")
    public void createParentComment() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.CommentController.createChildComment(..))")
    public void createChildComment() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.CommentController.getPageMyCommentedBoards(..))")
    public void getPageMyCommentedBoards() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.CommentController.getCommentsByBoardId(..))")
    public void getCommentsByBoardId() {}

    @Pointcut("execution(* com.aliens.backend.board.controller.CommentController.deleteComment(..))")
    public void deleteComment() {}
}
