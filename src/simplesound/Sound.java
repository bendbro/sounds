package simplesound;

import simplesound.wavefunction.IWaveFunction;

import java.util.Arrays;

/**
 * Created by ben on 6/11/2015.
 */
public class Sound {
    double sampleRate;
    byte[] data;

    /**
     * Create a new sound.
     * @param sampleRate The samples per second.
     * @param duration The duration the sound lasts in seconds.
     * @param pitch The frequency of the sound in hz.
     * @param magnitude The magnitide (between -1 and 1);
     * @param function The wave function.
     */
    public Sound(double sampleRate, double duration, double pitch, double magnitude, IWaveFunction function) {
        this.sampleRate = sampleRate;
        magnitude = 125.0 * Math.max(Math.min(magnitude,1.0),-1);
        int sample = 0;
        int samples = (int) (sampleRate * duration);
        data = new byte[samples];
        double constant = Math.PI * 2.0 / sampleRate * pitch;
        while(sample < samples) {
            data[sample] = (byte) (function.calculate(sample * constant) * magnitude);
            sample++;
        }
    }

    public Sound blend(double startTime, Sound other) {
        assert(other.sampleRate == sampleRate);

        int startIndex = (int) Math.max(0, sampleRate * startTime);
        int stopIndex = Math.min(data.length, startIndex + other.data.length);

        byte[] blended = new byte[data.length];
        for(int i = 0; i < startIndex; i++) {
            blended[i] = data[i];
        }
        for(int i = startIndex; i < stopIndex; i++) {
            blended[i] = (byte) (data[i] + other.data[i - startIndex] / 2.0);
        }
        for(int i = stopIndex; i < blended.length; i++) {
            blended[i] = data[i];
        }
        return new Sound(blended, other.sampleRate);
    }

    public Sound scale(double scale) {
        byte[] newdata = new byte[data.length];
        for(int i = 0; i < data.length; i++) {
            newdata[i] = (byte) (data[i] * scale);
        }
        return new Sound(newdata,sampleRate);
    }

    public Sound splice(double startTime, double stopTime) {
        int startIndex = (int) Math.max(0, sampleRate * startTime);
        int stopIndex = (int) Math.max(0, sampleRate * stopTime);

        byte[] spliced = new byte[stopIndex - startIndex];
        for(int i = startIndex; i < stopIndex; i++) {
            spliced[i - startIndex] = data[i];
        }

        return new Sound(spliced,sampleRate);
    }

    //TODO: prepend append,


    public double duration() {
        return sampleRate * data.length;
    }

    public Sound(byte[] data, double sampleRate) {
        this.data = data;
        this.sampleRate = sampleRate;
    }
}
