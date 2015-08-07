package su.vistar.servercontrol.entity;

public class ConnectionEntity {

    private int channelId;
    private int adapterId;
    private String adapterName;
    private long identifyStr;
    private int vehicleId;
    private String number;
    private int accountId;
    private int localPort;
    private int remotePort;
    private String remoteIp;
    private String deviceState;
    private String channelState;
    private String closeWay;
    private String exception;
    private String netstatState;
    private long timestamp;

    public int getChannelId() {
        return channelId;
    }

    public int getAdapterId() {
        return adapterId;
    }

    public String getAdapterName() {
        return adapterName;
    }

    public long getIdentifyStr() {
        return identifyStr;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getNumber() {
        return number;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getLocalPort() {
        return localPort;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public String getDeviceState() {
        return deviceState;
    }

    public String getChannelState() {
        return channelState;
    }

    public String getCloseWay() {
        return closeWay;
    }

    public String getException() {
        return exception;
    }

    public String getNetstatState() {
        return netstatState;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public void setAdapterId(int adapterId) {
        this.adapterId = adapterId;
    }

    public void setAdapterName(String adapterName) {
        this.adapterName = adapterName;
    }

    public void setIdentifyStr(long identifyStr) {
        this.identifyStr = identifyStr;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public void setDeviceState(String deviceState) {
        this.deviceState = deviceState;
    }

    public void setChannelState(String channelState) {
        this.channelState = channelState;
    }

    public void setCloseWay(String closeWay) {
        this.closeWay = closeWay;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setNetstatState(String netstatState) {
        this.netstatState = netstatState;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Connection [channelId=" + channelId + ", adapterId=" + adapterId + ", adapterName=" + adapterName + ", identifyStr=" + identifyStr
                + ", vehicleId=" + vehicleId + ", number=" + number + ", accountId=" + accountId + ", localPort=" + localPort
                + ", remotePort=" + remotePort + ", remoteIp=" + remoteIp + ", deviceState=" + deviceState + ", channelState=" + channelState
                + ", closeWay=" + closeWay + ", exception=" + exception + ", netstatState=" + netstatState + ", timestamp=" + timestamp + " ]";
    }

}
