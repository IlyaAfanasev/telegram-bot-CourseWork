package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.NotificationTask;

public interface NotificationTaskService {
    NotificationTask createNT(NotificationTask notificationTask);

    NotificationTask getNT(Long id);


}
