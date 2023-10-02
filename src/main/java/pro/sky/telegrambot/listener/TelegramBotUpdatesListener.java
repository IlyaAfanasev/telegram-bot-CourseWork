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

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {

            if (update.message().text().contains("start")) {
                createStartMessage(update);
            }

            logger.info("Processing update: {}", update);
            // Process your updates here
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
//метод для обработки команды "/start"
    private void createStartMessage(Update update) {
        Long chatId = update.message().chat().id();
        String userName = update.message().chat().firstName();
        String text = update.message().text();

        SendMessage message = new SendMessage(chatId, text+ ". Hello " + userName);
        sendMessage(message);

    }
//метод для отправки сообщения
    private void sendMessage(SendMessage message) {
        SendResponse response = telegramBot.execute(message);
        System.out.println(response.isOk());
        System.out.println(response.errorCode());
    }

}
