package su.vistar.db.entity;

public class GetImeiEntity {

    String sdp_imei;

    public String getSdp_imei() {
        return sdp_imei;
    }

    public void setSdp_imei(String sdp_imei) {
        this.sdp_imei = sdp_imei;
    }

    @Override
    public String toString() {
        return "GetImeiEntity{" + "sdp_imei=" + sdp_imei + '}';
    }

}
