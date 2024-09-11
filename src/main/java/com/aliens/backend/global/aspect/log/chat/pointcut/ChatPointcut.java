package com.aliens.backend.global.aspect.log.chat.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class ChatPointcut {
    @Pointcut("execution(* com.aliens.backend.chat.controller.ChatController.sendMessage(..))")
    public void sendMessage() {}

    @Pointcut("execution(* com.aliens.backend.chat.controller.ChatController.readMessage(..))")
    public void readMessage() {}

    @Pointcut("execution(* com.aliens.backend.chat.controller.ChatController.getChatSummaries(..))")
    public void getChatSummaries() {}

    @Pointcut("execution(* com.aliens.backend.chat.controller.ChatController.getMessages(..))")
    public void getMessages() {}

}
