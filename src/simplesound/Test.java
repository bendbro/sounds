package simplesound;

import simplesound.wavefunction.SawToothFunction;
import simplesound.wavefunction.SineFunction;

import javax.sound.sampled.LineUnavailableException;

/**
 * Created by ben on 6/11/2015.
 */
public class Test {
    //TODO: sound into stream, http over stream
    public static void main(String[] args) throws LineUnavailableException {
        Sound created = new Sound(44100,1,500,1.0,SineFunction.Instance);
        Recorder recorder = new Recorder(44100);
        new Thread(() -> {
            try {
                new Player(created).play();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                new Player(recorder.record(5).scale(25000.0)).play();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
