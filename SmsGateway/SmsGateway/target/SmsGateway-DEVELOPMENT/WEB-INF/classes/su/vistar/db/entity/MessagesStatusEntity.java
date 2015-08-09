package su.vistar.db.entity;

import java.util.LinkedList;
import java.util.List;

public class MessagesStatusEntity {

    List<MessageStatusEntity> messagesStatuses;

    public List<MessageStatusEntity> getMessagesStatuses() {
        return messagesStatuses;
    }

    public void setMessagesStatuses(List<MessageStatusEntity> messagesStatuses) {
        this.messagesStatuses = messagesStatuses;
    }

    public void addMarker(MessageStatusEntity messageStatus) {
        if (this.messagesStatuses == null) {
            this.messagesStatuses = new LinkedList<>();
        }
        this.messagesStatuses.add(messageStatus);
    }

    @Override
    public String toString() {
        return "MessagesStatusEntity{" + "messagesStatuses=" + messagesStatuses + '}';
    }

}
