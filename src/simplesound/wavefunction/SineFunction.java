package simplesound.wavefunction;

/**
 * Created by ben on 6/11/2015.
 */
public class SineFunction implements IWaveFunction {
    public static final SineFunction Instance = new SineFunction();

    @Override
    public double calculate(double index) {
        return Math.sin(index);
    }
}
