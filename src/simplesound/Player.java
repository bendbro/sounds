package simplesound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Created by ben on 6/11/2015.
 */
public class Player {
    private Sound sound;

    public Player(Sound sound) {
        this.sound = sound;
    }

    public void play() throws LineUnavailableException {
        AudioFormat af = new AudioFormat((float) sound.sampleRate,8,1,true,false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open();
        sdl.start();
        sdl.write(sound.data, 0, sound.data.length);
        sdl.drain();
        sdl.stop();
    }
}
