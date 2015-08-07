package su.vistar.servercontrol.servercomponents.checkers.various.vistarmarshrut.shadow.misc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CGaps {

    public CGap forw,  backw;

    public CGaps() {
        resetGaps();
    }

    public void resetGaps() {
        forw = new CGap();
        backw = new CGap();
    }

    public CGap[] toGapArray() {
        return new CGap[]{forw, backw};
    }

    public static class CGap {

        public long gap;
        public int num;

        public CGap() {
            this.gap = 0;
            this.num = -1;
        }

        public CGap(InputStream is) throws IOException {
            fromStream(is);
        }

        public CGap(int num, long gap) {
            this.num = num;
            this.gap = gap;
        }

        public void assign(CGap cGap) {
            this.num = cGap.num;
            this.gap = cGap.gap;
        }
        
        public void fromStream(InputStream is) throws IOException {
            DataInputStream dis = new DataInputStream(is);
            num = dis.readShort();
            gap = dis.readShort();
        }

        public void toStream(OutputStream os) throws IOException {
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeShort(num);
            dos.writeShort((short) (gap));
        }

        @Override
        public String toString() {
            return String.format("%s[num: %d; gap: %d]", getClass().getSimpleName(), num, gap);
        }

     }
}