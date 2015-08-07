package su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut;

import java.util.Date;
import su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow.ShadowClientListener;

public class ShadowClientAdapterHeir extends ShadowClientListener.ShadowClientAdapter {

    public Date changeSectionTime;
    public boolean isConnected = false;
    public Date connectedTime;
    public Date disconnectedTime;
    
//===================================================================================

    @Override
    public void onPSection(short routeId, short sectionId, short busId, int switchTime, short averageTime) {
        changeSectionTime = new Date();
    }
    
//===================================================================================    

    @Override
    public void onConnected() {
        if (!isConnected) {
            isConnected = true;
        }
        connectedTime = new Date();
    }
    
//===================================================================================

    @Override
    public void onDisconnected(Throwable disconnectThrowable) {
        if(isConnected) {
            isConnected = false;
        }
        disconnectedTime = new Date();
    }
}
