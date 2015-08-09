package su.vistar.db.entity;

public class CountEntity {

    String msg_status;
    int cnt;

    public String getMsg_status() {
        return msg_status;
    }

    public int getCnt() {
        return cnt;
    }

    public void setMsg_status(String msg_status) {
        this.msg_status = msg_status;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "CountEntity{" + "msg_status=" + msg_status + ", cnt=" + cnt + '}';
    }

}
