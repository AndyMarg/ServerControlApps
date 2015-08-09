package su.vistar.db.entity;

import java.util.Date;

public class MessageEntity {

    int msg_id;
    String msg_text;
    String msg_phone_numbers;
    String msg_sender_id;
    String msg_sender_name;
    String msg_status;
    String msg_comment;
    Date msg_creation_date;
    Date msg_send_date;
    int msg_app_id;

    public int getMsg_id() {
        return msg_id;
    }

    public String getMsg_text() {
        return msg_text;
    }

    public String getMsg_phone_numbers() {
        return msg_phone_numbers;
    }

    public String getMsg_sender_id() {
        return msg_sender_id;
    }

    public String getMsg_sender_name() {
        return msg_sender_name;
    }

    public String getMsg_status() {
        return msg_status;
    }

    public String getMsg_comment() {
        return msg_comment;
    }

    public Date getMsg_creation_date() {
        return msg_creation_date;
    }

    public Date getMsg_send_date() {
        return msg_send_date;
    }

    public int getMsg_app_id() {
        return msg_app_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public void setMsg_text(String msg_text) {
        this.msg_text = msg_text;
    }

    public void setMsg_phone_numbers(String msg_phone_numbers) {
        this.msg_phone_numbers = msg_phone_numbers;
    }

    public void setMsg_sender_id(String msg_sender_id) {
        this.msg_sender_id = msg_sender_id;
    }

    public void setMsg_status(String msg_status) {
        this.msg_status = msg_status;
    }

    public void setMsg_comment(String msg_comment) {
        this.msg_comment = msg_comment;
    }

    public void setMsg_creation_date(Date msg_creation_date) {
        this.msg_creation_date = msg_creation_date;
    }

    public void setMsg_send_date(Date msg_send_date) {
        this.msg_send_date = msg_send_date;
    }

    public void setMsg_app_id(int msg_app_id) {
        this.msg_app_id = msg_app_id;
    }

    public void setMsg_sender_name(String msg_sender_name) {
        this.msg_sender_name = msg_sender_name;
    }

    @Override
    public String toString() {
        return "MessageEntity{" + "msg_id=" + msg_id + ", msg_text=" + msg_text + ", msg_phone_numbers=" + msg_phone_numbers + ", msg_sender_id=" + msg_sender_id + ", msg_sender_name=" + msg_sender_name + ", msg_status=" + msg_status + ", msg_comment=" + msg_comment + ", msg_creation_date=" + msg_creation_date + ", msg_send_date=" + msg_send_date + ", msg_app_id=" + msg_app_id + '}';
    }

}
