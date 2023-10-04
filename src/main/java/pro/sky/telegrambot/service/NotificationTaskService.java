package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskService {
    NotificationTask createNT(NotificationTask notificationTask);

    List<NotificationTask> findByChatId(Long id);

    List<NotificationTask> findByLocalDateTime(LocalDateTime localDateTime);


}
