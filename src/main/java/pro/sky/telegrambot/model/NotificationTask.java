package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private Long chatId;
    private String text;
    private LocalDateTime localDateTime;

    public NotificationTask() {

    }

    public NotificationTask(Long id, Long chatId, String text, LocalDateTime localDateTime) {
        this.id = id;
        this.chatId = chatId;
        this.text = text;
        this.localDateTime = localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationTask nt)) {
            return false;
        }
        return id.equals(nt.id)
                && chatId.equals(nt.chatId)
                && localDateTime.equals(nt.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, localDateTime);
    }

    @Override
    public String toString() {
        return
                text + ' ' + localDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

}
