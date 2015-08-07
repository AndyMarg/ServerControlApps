
package su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow;

import su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow.misc.CGaps;
import su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow.misc.GPSPoint;

/**
 * Listener for shadow client events
 * 
 * @author Sergey Ponomarev <serggt1@yandex.ru>
 */
public interface ShadowClientListener {

    /** обработчик события: ShadowClient подключился к серверу */
    public void onConnected();
    /** обработчик события: ShadowClient отключился или не смог подключиться к серверу */
    public void onDisconnected(Throwable disconnectThrowable);

    /** обработчик события: сервер передал информацию об изменении каких то параметров секции */
    public void onPSection(short routeId, short sectionId, short busId, int switchTime, short averageTime);
    /** обработчик события: сервер передал информацию о том, что автобус отключился */
    public void onPBusDisconnect(short busId);
    /** обработчик события: сервер передал информацию об автобусе */
    public void onPBus(short busId, short routeId, short sectionId, int switchTime, CGaps.CGap forwardGap, CGaps.CGap backwardGap);
    /** обработчик события: сервер передал информацию об абсолютных координатах автобуса */
    public void onPGPSPosition(short busId, GPSPoint point);
    /** обработчик события: сервер передал информацию о том, что номер автобуса изменился */
    public void onPBusNumberChange(short busId, short newNumber);

    /** Listener Adapter. */
    public static class ShadowClientAdapter  implements ShadowClientListener {

        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(Throwable disconnectThrowable) {
        }

        @Override
        public void onPSection(short routeId, short sectionId, short busId, int switchTime, short averageTime) {
        }

        @Override
        public void onPBusDisconnect(short busId) {
        }

        @Override
        public void onPBus(short busId, short routeId, short sectionId, int switchTime, CGaps.CGap forwardGap, CGaps.CGap backwardGap) {
        }

        @Override
        public void onPGPSPosition(short busId, GPSPoint point) {
        }

        @Override
        public void onPBusNumberChange(short busId, short newNumber) {
        }
    }
}
