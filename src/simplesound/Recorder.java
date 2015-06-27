package simplesound;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by ben on 6/12/2015.
 */
public class Recorder {
    private float sampleRate;

    public Recorder(float sampleRate) {
        this.sampleRate = sampleRate;
    }

    public Sound record(long time) throws LineUnavailableException {
        AudioFormat af = new AudioFormat(sampleRate,8,1,true,true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);

        line.open(af);
        line.start();
        ByteArrayOutputStream out  = new ByteArrayOutputStream();
        int numBytesRead;
        byte[] data = new byte[line.getBufferSize() / 5];
        long stop = System.currentTimeMillis() + time * 1000;
        while (System.currentTimeMillis() < stop) {
            numBytesRead = line.read(data, 0, data.length);
            out.write(data, 0, numBytesRead);
        }
        line.flush();
        line.stop();
        line.close();

        return new Sound(out.toByteArray(), sampleRate);
    }
}
