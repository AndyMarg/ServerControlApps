package su.vistar.servercontrol.entity;

import java.util.Date;

public class ServerEntity {

    public String serverName;
    public boolean isActive;
    public String statusDescription;
    public Date lastCheckTime;
    public Date lastSuccessCheckTime;
    public Date serverStopTime;

    public String checkErrorDescription;
    public boolean isCheckError;

    public String getServerName() {
        return serverName;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public Date getLastCheckTime() {
        return lastCheckTime;
    }

    public Date getLastSuccessCheckTime() {
        return lastSuccessCheckTime;
    }

    public Date getServerStopTime() {
        return serverStopTime;
    }

    public String getCheckErrorDescription() {
        return checkErrorDescription;
    }

    public boolean isIsCheckError() {
        return isCheckError;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public void setLastCheckTime(Date lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }

    public void setLastSuccessCheckTime(Date lastSuccessCheckTime) {
        this.lastSuccessCheckTime = lastSuccessCheckTime;
    }

    public void setServerStopTime(Date serverStopTime) {
        this.serverStopTime = serverStopTime;
    }

    public void setCheckErrorDescription(String checkErrorDescription) {
        this.checkErrorDescription = checkErrorDescription;
    }

    public void setIsCheckError(boolean isCheckError) {
        this.isCheckError = isCheckError;
    }

    @Override
    public String toString() {
        return "ServerEntity{" + "serverName=" + serverName + ", isActive=" + isActive + ", statusDescription=" + statusDescription + ", lastCheckTime=" + lastCheckTime + ", lastSuccessCheckTime=" + lastSuccessCheckTime + ", serverStopTime=" + serverStopTime + ", checkErrorDescription=" + checkErrorDescription + ", isCheckError=" + isCheckError + '}';
    }
    
    

}
