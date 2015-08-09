package su.vistar.db.entity;

public class GetMessageEntity {

    String sd_name;
    String app_name;
    String app_key;
    String msg_phone_numbers;
    String msg_text;
    String msg_comment;

    public String getSd_name() {
        return sd_name;
    }

    public String getMsg_phone_numbers() {
        return msg_phone_numbers;
    }

    public String getMsg_text() {
        return msg_text;
    }

    public String getMsg_comment() {
        return msg_comment;
    }

    public String getApp_name() {
        return app_name;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setSd_name(String sd_name) {
        this.sd_name = sd_name;
    }

    public void setMsg_phone_numbers(String msg_phone_numbers) {
        this.msg_phone_numbers = msg_phone_numbers;
    }

    public void setMsg_text(String msg_text) {
        this.msg_text = msg_text;
    }

    public void setMsg_comment(String msg_comment) {
        this.msg_comment = msg_comment;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    @Override
    public String toString() {
        return "GetMessageEntity{" + "sd_name=" + sd_name + ", app_name=" + app_name + ", app_key=" + app_key + ", msg_phone_numbers=" + msg_phone_numbers + ", msg_text=" + msg_text + ", msg_comment=" + msg_comment + '}';
    }

}
