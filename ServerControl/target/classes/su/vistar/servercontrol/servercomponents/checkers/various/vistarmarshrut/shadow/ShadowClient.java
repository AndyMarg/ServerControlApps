package su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.Date;
import su.vistar.logging.LoggerManager;
import static su.vistar.logging.helpers.LogFormatHelpers.decimal;
import static su.vistar.logging.helpers.LogFormatHelpers.hex;
import su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow.ShadowClientListener.ShadowClientAdapter;
import su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow.misc.CGaps;
import su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow.misc.CGaps.CGap;
import su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow.misc.GPSPoint;

/**
 * VIStar Marshrut Server shadow client.
 *
 * Shadow client connects to server and receives it's current data until
 * stopped.
 *
 * @author Sergey Ponomarev <serggt1@yandex.ru>
 */
public class ShadowClient {

    private static final LoggerManager log = new LoggerManager();
    /**
     * Server host name or ip address
     */
    private String serverHost;
    /**
     * Server's shadow port number
     */
    private int serverPort;
    /**
     * Password used to login to server
     */
    private int password;
    /**
     * Current connect and communication thread
     */
    private volatile ShadowCommunicationThread communicationThread;
    /**
     * Mutex which synchronizes access to data between the connection to shadow
     * server and connections to clients.
     */
    public final Object interServerMutex = new Object();
    /**
     * Event listener
     */
    private volatile ShadowClientListener listener;
    private static final int P_SECTION = 0xAAA;
    private static final int P_BUS = 0xAAB;
    private static final int P_BUS_DISCONNECT = 0xAAC;
    private static final int P_GPS_POSITION = 0xAAD;
    private static final int P_BUS_NUMBER_CHANGE = 0xAAE;
    private static final int P_ANNOUNCE_BUS_STOP = 0x1AED;

    /**
     * Creates a new instance of Shadow client.
     */
    public ShadowClient(String host, int port, int password, ShadowClientListener listener) {
        this.serverHost = host;
        this.serverPort = port;
        this.password = password;

        setListener(listener);
    }

    /**
     * Starts shadow client: connection and data transfer
     */
    public void start() {
        stop();
        log.info("starting shadow client");
        communicationThread = new ShadowCommunicationThread();
        communicationThread.start();
    }

    /**
     * Interrupts and stops shadow client
     */
    public void stop() {
        if (communicationThread == null) {
            return;
        }
        log.info("stopping shadow client");
        communicationThread.terminate();
        communicationThread = null;
    }

    public void setListener(ShadowClientListener listener) {
        if (listener == null) {
            this.listener = new ShadowClientAdapter();
        } else {
            this.listener = listener;
        }
    }

    /**
     * Thread that connects to and communicates with server
     */
    private class ShadowCommunicationThread extends Thread {

        /**
         * Flag means this thread is alive
         */
        private volatile boolean isAlive = true;
        /**
         * Server shadow connection socket
         */
        private Socket socket;
        private DataInputStream dis;
        private DataOutputStream dos;

        public ShadowCommunicationThread() {
            super("VPArrivalServer-ShadowClientCommunication-Thread");
        }

        @Override
        public void run() {
            try {
                run0();
            } catch (Throwable t) {
                log.severe(t, "Ошибка в shadow client");
            } finally {
                log.info("communication thread exited");
            }
        }

        private void run0() {
            // try to connect and communicate until this thread should be alive
            while (isAlive) {
                Throwable disconnectThrowable = null;
                try {
                    // connect to server
                    connectToServer(serverHost, serverPort);
                    // login
                    loginToServer();
                    listener.onConnected();
                    // do data transfer
                    communicate();
                } catch (EOFException e) {                    
                    log.finer(e, "Ошибка при получении данных через shadow client");
                    disconnectThrowable = e;
                } catch (IOException e) {
                    if (isAlive) {
                        log.severe(e, "Ошибка при получении данных через shadow client");
                    }
                    disconnectThrowable = e;
                } catch (Throwable t) {
                    log.severe(t, "Ошибка при получении данных через shadow client");
                    disconnectThrowable = t;
                }

                try {
                    listener.onDisconnected(disconnectThrowable);
                } catch (Throwable t) {
                    log.severe(t, "Ошибка в shadow client onDisconnected");
                }

                // sleep before next try
                try {
                    if (isAlive) {
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException ex) {
                }
            }
        }

        /**
         * Stops this thread. Thread will be terminated as soon as possible
         */
        public void terminate() {
            isAlive = false;
            closeConnection();
            interrupt();
        }

        /**
         * Connects to server or throws an exception.
         *
         * @param host Server host
         * @param port Server port
         *
         * @throws java.io.IOException If connection was failed
         */
        private void connectToServer(String host, int port) throws IOException {
            closeConnection();
            log.info("connecting to shadow server at {0}:{1}", host, decimal(port));
            socket = new Socket(Inet4Address.getByName(host), port);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        }

        /**
         * Closes connection to server
         */
        private void closeConnection() {
            if (socket == null) {
                return;
            }

            log.info("closing connection to shadow server");

            try {
                socket.close();
            } catch (IOException e) {
            }

            socket = null;
            dis = null;
            dos = null;
        }

        /**
         * Logins to shadow server.
         *
         * If login fails the server will just close the connection.
         *
         * @throws java.io.IOException If some I/O error occurs
         */
        private void loginToServer() throws IOException {
            dos.writeInt(password);
            dos.flush();
        }

        /**
         * Communicates with shadow server until some error occured or thread
         * will be terminated
         */
        private void communicate() throws IOException {
            int lastcmd = 0;
            while (isAlive) {
                // read the command
                int cmd = dis.readShort();
                synchronized (interServerMutex) {
                    switch (cmd) {
                        //сервер передает информацию об изменении каких то параметров секции
                        case P_SECTION: {
                            //считываем значения необходимые для нахождения секции
                            //и установки ее параметров
                            short routeId = dis.readShort();
                            short sectionId = dis.readShort();
                            short busId = dis.readShort();
                            int switchTime = dis.readInt();
                            short averageTime = dis.readShort();

                            log.finest("P_SECTION [routeID:{0};secID:{1};busID:{2};switchTime:{3};avTime:{4}]",
                                    decimal(routeId), decimal(sectionId), decimal(busId), new Date(((long) switchTime) * 1000), decimal(averageTime));

                            notifyOnPSection(routeId, sectionId, busId, switchTime, averageTime);
                            break;
                        }
                        //сервер передает информацию о том, что автобус отключился
                        case P_BUS_DISCONNECT: {
                            short busId = dis.readShort();

                            log.finest("P_BUS_DISCONNECT [busID:{0}]", decimal(busId));

                            notifyOnPBusDisconnect(busId);
                            break;
                        }
                        //сервер передает информацию об автобусе
                        case P_BUS: {
                            //считываем основные параметры
                            short busId = dis.readShort();
                            short routeId = dis.readShort();
                            short sectionId = dis.readShort();
                            int switchTime = dis.readInt();
                            CGap forwardGap = new CGap(dis);
                            CGap backwardGap = new CGap(dis);

                            log.finest("P_BUS [busID:{0};routeID:{1};secID:{2};switchTime:{3};forwBusNum:{4};forwGap:{5};backBusNum:{6};backGap:{7}]",
                                    decimal(busId), decimal(routeId), decimal(sectionId), new Date(((long) switchTime) * 1000), decimal(forwardGap.num), decimal(forwardGap.gap), decimal(backwardGap.num), decimal(backwardGap.gap));

                            notifyOnPBus(busId, routeId, sectionId, switchTime, forwardGap, backwardGap);
                            break;
                        }
                        //сервер передает информацию об абсолютных координатах автобуса
                        case P_GPS_POSITION: {
                            short busId = dis.readShort();
                            //считываем тип gpsPosition из сокета
                            GPSPoint point = new GPSPoint(dis);                            

//                           log.finest("P_GPS_POSITION");
                            log.finest("P_GPS_POSITION [busID:{0};gpsPoint:{1}]", decimal(busId), point);

                            notifyOnPGPSPosition(busId, point);
                            break;
                        }
                        case P_BUS_NUMBER_CHANGE: {
                            short busId = dis.readShort();
                            short newNumber = dis.readShort();

                            log.finest("P_BUS_NUMBER_CHANGE [busID:{0};newNumber:{1}]",
                                    decimal(busId), decimal(newNumber));

                            notifyOnPBusNumberChange(busId, newNumber);
                            break;
                        }

                        case P_ANNOUNCE_BUS_STOP: {
                            short busId = dis.readShort();
                            int annType = dis.readByte() & 0xFF;
                            int stopID = dis.readInt();

                            log.finest("P_ANNOUNCE_BUS_STOP [busID:%d;annType:%d;stopID:%d]",
                                    busId, annType, stopID);
                            break;
                        }

                        default:
                            log.fine("unknown command read 0x{0}, lastcmd: 0x{1}", hex(cmd), hex(lastcmd));
                    }
                }
                lastcmd = cmd;
            }
        }
    }

    private void notifyOnPSection(short routeId, short sectionId, short busId, int switchTime, short averageTime) {
        try {
            listener.onPSection(routeId, sectionId, busId, switchTime, averageTime);
        } catch (Throwable e) {
            log.severe(e, "Ошибка при обработке onPSection({0}, {1}, {2}, {3}, {4})",
                    routeId, sectionId, busId, switchTime, averageTime);
        }
    }

    private void notifyOnPBusDisconnect(short busId) {
        try {
            listener.onPBusDisconnect(busId);
        } catch (Throwable e) {
            log.severe(e, "Ошибка при обработке onPBusDisconnect({0})", busId);
        }
    }

    private void notifyOnPBus(short busId, short routeId, short sectionId, int switchTime, CGaps.CGap forwardGap, CGaps.CGap backwardGap) {
        try {
            listener.onPBus(busId, routeId, sectionId, switchTime, forwardGap, backwardGap);
        } catch (Throwable e) {
            log.severe(e, "Ошибка при обработке onPBus({0}, {1}, {2}, {3}, {4}, {5})",
                    busId, routeId, sectionId, switchTime, forwardGap, backwardGap);
        }
    }

    private void notifyOnPGPSPosition(short busId, GPSPoint point) {
        try {
            listener.onPGPSPosition(busId, point);
        } catch (Throwable e) {
            log.severe(e, "Ошибка при обработке onPGPSPosition({0}, {1})", busId, point);
        }
    }

    private void notifyOnPBusNumberChange(short busId, short newNumber) {
        try {
            listener.onPBusNumberChange(busId, newNumber);
        } catch (Throwable e) {
            log.severe(e, "Ошибка при обработке onPBusNumberChange({0}, {1})", busId, newNumber);
        }
    }

}
