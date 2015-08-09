package su.vistar.db.entity;

import java.util.LinkedList;
import java.util.List;

public class MessagesEntity {

    List<MessageEntity> messages;

    public MessagesEntity() {
    }
    
    MessagesEntity(List<MessageEntity> messages){
        this.messages = messages;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    public void addMarker(MessageEntity message) {
        if (this.messages == null) {
            this.messages = new LinkedList<>();
        }
        this.messages.add(message);
    }

    @Override
    public String toString() {
        return "MessagesEntity{" + "messages=" + messages + '}';
    }
    
    
}
