package com.aliens.backend.global.aspect.log.notification.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class NotificationPointcut {
    @Pointcut("execution(* com.aliens.backend.notification.controller.NotificationController.registerFcmToken(..))")
    public void registerFcmToken() {}

    @Pointcut("execution(* com.aliens.backend.notification.controller.NotificationController.getFcmStatus(..))")
    public void getFcmStatus() {}

    @Pointcut("execution(* com.aliens.backend.notification.controller.NotificationController.changeAcceptation(..))")
    public void changeAcceptation() {}

    @Pointcut("execution(* com.aliens.backend.notification.controller.NotificationController.getNotifications(..))")
    public void getNotifications() {}

    @Pointcut("execution(* com.aliens.backend.notification.controller.NotificationController.readNotification(..))")
    public void readNotification() {}

}
