
package su.vistar.servercontrol.entity;

public class BusArrivalEntity {
    
    private String busRoute;
    private int arrivalTime;
    private int rideTime;
    private double lat;
    private  double lon;

    public String getBusRoute() {
        return busRoute;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getRideTime() {
        return rideTime;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setBusRoute(String busRoute) {
        this.busRoute = busRoute;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setRideTime(int rideTime) {
        this.rideTime = rideTime;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Bus [busRoute=" + busRoute + ", arrivalTime=" + arrivalTime + ", rideTime=" + rideTime + ", lat=" + lat + ", lon=" + lon + "]";
    }
    
    
}
