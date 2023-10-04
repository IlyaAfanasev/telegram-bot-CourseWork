package pro.sky.telegrambot.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class NotificationTaskServiceImpl implements NotificationTaskService{
    Logger logger = LoggerFactory.getLogger(NotificationTask.class);

    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskServiceImpl(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @Override
    public NotificationTask createNT(NotificationTask notificationTask) {

        return notificationTaskRepository.save(notificationTask);
    }

    @Override
    public List<NotificationTask> findByChatId(Long id) {
        return notificationTaskRepository.findByChatId(id);
    }


    @Override
    public List<NotificationTask> findByLocalDateTime(LocalDateTime localDateTime) {
        return notificationTaskRepository.findByLocalDateTime(localDateTime);
    }
}
