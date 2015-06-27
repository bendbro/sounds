import simplesound.Player;
import simplesound.Sound;
import simplesound.wavefunction.SineFunction;

import javax.sound.sampled.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ben on 6/11/2015.
 */
public class SoundTest {
    /**
     * http://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/AudioFormat.html
     * https://community.oracle.com/thread/1273219?start=0&tstart=0
     * http://stackoverflow.com/questions/1932490/java-generating-sound
     * https://www.youtube.com/watch?v=YJmUkNTBa8s
     * http://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/SourceDataLine.html
     * http://csweb.cs.wfu.edu/~burg/CCLI/Templates/flash.php?file=Chapter2/Non-Sinusoidal_Waves.swf
     * @param args
     * @throws LineUnavailableException
     */
    public static void main(String[] args) throws LineUnavailableException {
        byte[] a = buffered(44100,3,500,.5);
        byte[] b = buffered(44100,3,4000,.5);
        byte[] blended = blend(a,b);
        play(44100,a);
        play(44100,b);
        play(44100,blended);
    }

    /**
     * @param sampleRate Sound intensity (44100 works well)
     * @param duration Duration of tone in seconds [0,infinity)
     * @param pitch Pitch of tone in Hz [20,20000] good for humans
     * @param magnitude Volume [-1,1] allowed
     * @throws LineUnavailableException
     */
    public static byte[] buffered(double sampleRate, double duration, double pitch, double magnitude) throws LineUnavailableException {
        magnitude = 125.0 * Math.max(Math.min(magnitude,1.0),-1);
        int sample = 0;
        int samples = (int) (sampleRate * duration);
        byte[] buffer = new byte[samples];
        double constant = Math.PI * 2.0 / sampleRate * pitch;
        while(sample < samples) {
            buffer[sample] = (byte)(Math.sin(sample * constant) * magnitude);
            sample++;
        }
        return buffer;
    }

    public static byte[] blend(byte[] a, byte[] b) {
        byte[] blended = new byte[Math.min(a.length,b.length)];
        for(int index = 0; index < blended.length; index++) {
            blended[index] = (byte) (((float)a[index] + (float)b[index]) / 2.0f);
        }
        return blended;
    }

    /**
     * Play a sound at a given sample rate with 1 byte per sample.
     * @param sampleRate The rate.
     * @param data The data.
     */
    public static void play(double sampleRate, byte[] data) throws LineUnavailableException {
        AudioFormat af = new AudioFormat((float) sampleRate,8,1,true,false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open();
        sdl.start();
        sdl.write(data,0,data.length);
        sdl.drain();
        sdl.stop();
    }

    private static void onTheFly() throws LineUnavailableException {
        long time = 5000;
        double pitch = 1.0;
        double magnitude = 1.0; // 0 - 125
        float sampleRate = 44100.0f; // samples per second
        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat(sampleRate,8,1,true,false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open();
        sdl.start();

        pitch = 2.13 * Math.max(Math.min(pitch,1.0), 0.0);
        magnitude = 125.0 * Math.max(Math.min(magnitude,1.0), 0.0);

        int tonesIndex = 0;
        long end = System.currentTimeMillis() + time;
        while(System.currentTimeMillis() < end) {
            buf[0] = (byte)(Math.sin(tonesIndex * pitch) * magnitude);
            sdl.write(buf,0,1);
            tonesIndex++;
        }

        sdl.drain();
        sdl.stop();
    }
}
