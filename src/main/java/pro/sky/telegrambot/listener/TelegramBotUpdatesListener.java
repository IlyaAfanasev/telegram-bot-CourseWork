package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.configuration.TelegramBotConfiguration;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private  NotificationTaskService notificationTaskService;

    public TelegramBotUpdatesListener() {
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message().text().isBlank()) {
                sendDefaultMessage(update);
            }
            else if (update.message().text().startsWith("/")) {
                switch (update.message().text()) {
                    case "/start" -> sendStartMessage(update);
                    case "/help" -> sendHelpMessage(update);
                    default -> sendDefaultMessage(update);
                }

            }
            else {
                createNotificationTask(update);

            }


        logger.info("Processing update: {}", update);
        // Process your updates here
    });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
}

    private void createNotificationTask(Update update) {
        logger.info("Processing create Notification Task");
        String string = update.message().text();

//        Pattern pattern = Pattern.compile("\\s*([\\d+\\.\\:\\s]{16})\\s*([\\W+]+)\\s*");
        Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})\\s*([\\.+]+)");
        Matcher matcher = pattern.matcher(string);


        if (matcher.matches()) {

            String date = matcher.group(1);
            String text = matcher.group(2);
            LocalDateTime localDateTime;
            try {
                localDateTime = LocalDateTime.parse(date
                        , DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            } catch (DateTimeParseException e) {
                throw new DateTimeParseException(e.getMessage(), e.getParsedString(), e.getErrorIndex());
            }
            NotificationTask nT = new NotificationTask(null, update.message().chat().id()
                    , text, localDateTime);
            telegramBot.execute(new SendMessage(update.message().chat().id()
            , "Создана запись " +notificationTaskService.createNT(nT).toString()));
        } else {
            System.out.println("{hty nt,t))");
        }

    }



    //default обработка команды
    private void sendDefaultMessage(Update update) {
        Long chatId = update.message().chat().id();
        String userName = update.message().chat().firstName();
        String text = update.message().text();

        SendMessage message = new SendMessage(chatId,"Sorry " +userName+
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

        SendMessage message = new SendMessage(chatId,"Sorry " +userName+
                ". Ничем не могу помочь))");
        sendMessage(message);

    }

    //метод для отправки сообщения
    private void sendMessage(SendMessage message) {
        SendResponse response = telegramBot.execute(message);
        System.out.println(response.isOk());
        System.out.println(response.errorCode());
    }

}
