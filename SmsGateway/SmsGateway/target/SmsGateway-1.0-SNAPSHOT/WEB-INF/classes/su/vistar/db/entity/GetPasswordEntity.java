package su.vistar.db.entity;

public class GetPasswordEntity {

    String sdp_imei;
    String sdp_password;

    public String getSdp_imei() {
        return sdp_imei;
    }

    public String getSdp_password() {
        return sdp_password;
    }

    public void setSdp_imei(String sdp_imei) {
        this.sdp_imei = sdp_imei;
    }

    public void setSdp_password(String sdp_password) {
        this.sdp_password = sdp_password;
    }



    @Override
    public String toString() {
        return "GetPasswordEntity{" + "spd_imei=" + sdp_imei + ", sdp_password=" + sdp_password + '}';
    }

}
