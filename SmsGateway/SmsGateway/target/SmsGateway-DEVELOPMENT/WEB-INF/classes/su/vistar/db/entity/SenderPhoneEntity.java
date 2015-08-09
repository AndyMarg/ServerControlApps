package su.vistar.db.entity;

public class SenderPhoneEntity {

    int sdp_sender_id;
    String spd_imei;
    String sdp_password;

    public int getSdp_sender_id() {
        return sdp_sender_id;
    }

    public String getSpd_imei() {
        return spd_imei;
    }

    public void setSdp_sender_id(int sdp_sender_id) {
        this.sdp_sender_id = sdp_sender_id;
    }

    public void setSpd_imei(String spd_imei) {
        this.spd_imei = spd_imei;
    }

    public String getSdp_password() {
        return sdp_password;
    }

    public void setSdp_password(String sdp_password) {
        this.sdp_password = sdp_password;
    }

    @Override
    public String toString() {
        return "SenderPhoneEntity{" + "sdp_sender_id=" + sdp_sender_id + ", spd_imei=" + spd_imei + ", sdp_password=" + sdp_password + '}';
    }

}
