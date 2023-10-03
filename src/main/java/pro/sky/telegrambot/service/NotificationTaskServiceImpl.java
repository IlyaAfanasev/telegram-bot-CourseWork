package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
@Service
public class NotificationTaskServiceImpl implements NotificationTaskService{

    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskServiceImpl(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @Override
    public NotificationTask createNT(NotificationTask notificationTask) {
        return notificationTaskRepository.save(notificationTask);
    }

    @Override
    public NotificationTask getNT(Long id) {
        return notificationTaskRepository.getById(id);
    }
}
