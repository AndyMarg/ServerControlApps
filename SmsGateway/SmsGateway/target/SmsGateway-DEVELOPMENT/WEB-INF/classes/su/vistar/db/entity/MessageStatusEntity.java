package su.vistar.db.entity;

import java.util.Date;

public class MessageStatusEntity {

    String messageSendDate;
    int messageId;
    String status;

    public String getMessageSendDate() {
        return messageSendDate;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getStatus() {
        return status;
    }

    public void setMessageSendDate(String messageSendDate) {
        this.messageSendDate = messageSendDate;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MessageStatusEntity{" + "messageSendDate=" + messageSendDate + ", messageId=" + messageId + ", status=" + status + '}';
    }

}
