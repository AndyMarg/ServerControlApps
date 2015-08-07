package su.vistar.servercontrol.entity;

import java.util.LinkedList;
import java.util.List;

public class BusesEntity {

    private String status;
    private List<BusArrivalEntity> busArrival;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BusArrivalEntity> getBusArrival() {
        return busArrival;
    }

    public void setBusArrival(List<BusArrivalEntity> busArrival) {
        this.busArrival = busArrival;
    }

    public void addBus(BusArrivalEntity bus) {
        if (this.busArrival == null) {
            this.busArrival = new LinkedList<>();
        }
        this.busArrival.add(bus);
    }

    @Override
    public String toString() {
        return "BusArrival [busArrival=" + busArrival + "]";
    }
}
