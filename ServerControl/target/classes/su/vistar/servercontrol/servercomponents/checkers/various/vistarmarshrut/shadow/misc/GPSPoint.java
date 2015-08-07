
package su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow.misc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import util.io.littleendian.LEDataInputStream;
import util.io.littleendian.LEDataOutputStream;

/**
 *
 * @author Sergey Ponomarev <serggt1@yandex.ru>
 */
public class GPSPoint {
    public double lat = 0;
    public double lon = 0;
    public long timestamp = 0;
    public int speed = 0;
    public int run = 0;

    public GPSPoint() {
    }

    public GPSPoint(InputStream is) throws IOException {
        fromStream(is);
    }

    public void toStream(OutputStream os) throws IOException {
        LEDataOutputStream dos = new LEDataOutputStream(os);
        dos.writeDouble(lat);
        dos.writeDouble(lon);
        dos.writeLong(timestamp/1000);
        dos.writeInt(speed);
        dos.writeInt(run);
    }

    public void fromStream(InputStream is) throws IOException {
        // NOTICE: Reading from vistarServer seems to be bugged
        LEDataInputStream dis = new LEDataInputStream(is);
        lat = dis.readDouble();
        lon = dis.readDouble();
        timestamp = dis.readLong() * 1000L;
        speed = dis.readInt();
        run = dis.readInt();
    }

    @Override
    public String toString() {
        return String.format("GPSPoint[lat:%.6f;lon:%.6f;speed:%d;run:%d;date:%s]",
                lat, lon, speed, run, new Date(timestamp));
    }
}
