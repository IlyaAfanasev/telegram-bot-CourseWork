package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private NotificationTaskService notificationTaskService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message().text().isBlank()) {
                sendDefaultMessage(update);
            } else if (update.message().text().startsWith("/")) {
                switch (update.message().text()) {
                    case "/start" -> sendStartMessage(update);
                    case "/help" -> sendHelpMessage(update);
                    case "/get" -> sendGetMessage(update);
                    default -> sendDefaultMessage(update);
                }

            } else {
                createNotificationTask(update);

            }


            logger.info("Processing update: {}", update);
            // Process your updates here
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendGetMessage(Update update) {
        Long chatId = update.message().chat().id();
        List<NotificationTask> notificationTaskList = notificationTaskService.findByChatId(chatId);
        ListIterator<NotificationTask> iterator = notificationTaskList.listIterator();
        while (iterator.hasNext()) {
            iterator.forEachRemaining(this::sendNotificationTask);
        }
    }

    //мониторинг актуальных задач и отправка пользователю
    @Scheduled(cron = "0 0/1 * * * *")
    public void monitoringOfCurrentTasks() {
        logger.info("Was invoked method for monitoring of current tasks");
        LocalDateTime lDT = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> notificationTaskList = notificationTaskService.findByLocalDateTime(lDT);
        ListIterator<NotificationTask> iterator = notificationTaskList.listIterator();
        while (iterator.hasNext()) {
            iterator.forEachRemaining(this::sendNotificationTask);
        }

    }

    //создание  message из NotificationTask и отправка
    private void sendNotificationTask(NotificationTask notificationTask) {
        SendMessage message = new SendMessage(notificationTask.getChatId()
                , notificationTask.toString());
        sendMessage(message);
    }


    private void createNotificationTask(Update update) {
        logger.info("Processing create Notification Task");
        String string = update.message().text();

        Pattern pattern = Pattern.compile("([\\d.:\\s]{16})\\s+(.+)");
        Matcher matcher = pattern.matcher(string);


        if (matcher.matches()) {

            String date = matcher.group(1);
            String text = matcher.group(2);
            LocalDateTime localDateTime;
            try {
                localDateTime = LocalDateTime.parse(date
                        , DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            } catch (DateTimeParseException e) {
                 sendDefaultMessage(update);
                 return;
            }
            NotificationTask nT = new NotificationTask(null, update.message().chat().id()
                    , text, localDateTime);
            SendMessage message = new SendMessage(update.message().chat().id()
                    , "Создана запись " + notificationTaskService.createNT(nT).toString());
            sendMessage(message);
        } else {
            sendDefaultMessage(update);
        }

    }

    //default обработка команды
    private void sendDefaultMessage(Update update) {
        Long chatId = update.message().chat().id();
        String userName = update.message().chat().firstName();
        String text = update.message().text();

        SendMessage message = new SendMessage(chatId, "Sorry " + userName +
                ". Непонятный набор буков))");
        sendMessage(message);
    }


    //метод для обработки команды "/start"
    private void sendStartMessage(Update update) {
        Long chatId = update.message().chat().id();
        String userName = update.message().chat().firstName();
        SendMessage message = new SendMessage(chatId, " Hello " + userName);

        sendMessage(message);

    }

    //метод для обработки команды "/help"
    private void sendHelpMessage(Update update) {
        Long chatId = update.message().chat().id();
        String userName = update.message().chat().firstName();
        String text = update.message().text();

        SendMessage message = new SendMessage(chatId, "Sorry " + userName +
                ". Ничем не могу помочь))");
        sendMessage(message);

    }

    //метод для отправки сообщения
    private void sendMessage(SendMessage message) {
        SendResponse response = telegramBot.execute(message);

        if (response.errorCode() != 0) {
            logger.error(String.valueOf(response.errorCode()));

        } else {
            logger.info(String.valueOf(response.isOk()));
        }
    }

}
