package su.vistar.db.entity;

public class ApplicationEntity {

    int app_id;
    String app_name;
    String app_key;

    public int getApp_id() {
        return app_id;
    }

    public String getApp_name() {
        return app_name;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    @Override
    public String toString() {
        return "ApplicationEntity{" + "app_id=" + app_id + ", app_name=" + app_name + ", app_key=" + app_key + '}';
    }
    
    
}
