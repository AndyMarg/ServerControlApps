package su.vistar.db.entity;

public class SenderEntity {

    int sd_id;
    String sd_name;

    public int getSd_id() {
        return sd_id;
    }

    public String getSd_name() {
        return sd_name;
    }

    public void setSd_id(int sd_id) {
        this.sd_id = sd_id;
    }

    public void setSd_name(String sd_name) {
        this.sd_name = sd_name;
    }

    @Override
    public String toString() {
        return "SenderEntity{" + "sd_id=" + sd_id + ", sd_name=" + sd_name + '}';
    }
    
    
}
