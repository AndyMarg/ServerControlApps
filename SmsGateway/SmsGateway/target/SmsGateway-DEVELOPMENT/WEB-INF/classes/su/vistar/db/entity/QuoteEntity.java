package su.vistar.db.entity;

public class QuoteEntity {

    int qt_app_id;
    int qt_max_per_hour;
    int qt_same_min_interval;

    public int getQt_app_id() {
        return qt_app_id;
    }

    public int getQt_max_per_hour() {
        return qt_max_per_hour;
    }

    public int getQt_same_min_interval() {
        return qt_same_min_interval;
    }

    public void setQt_app_id(int qt_app_id) {
        this.qt_app_id = qt_app_id;
    }

    public void setQt_max_per_hour(int qt_max_per_hour) {
        this.qt_max_per_hour = qt_max_per_hour;
    }

    public void setQt_same_min_interval(int qt_same_min_interval) {
        this.qt_same_min_interval = qt_same_min_interval;
    }

    @Override
    public String toString() {
        return "QuoteEntity{" + "qt_app_id=" + qt_app_id + ", qt_max_per_hour=" + qt_max_per_hour + ", qt_same_min_interval=" + qt_same_min_interval + '}';
    }

}
