package simplesound.wavefunction;

/**
 * Created by ben on 6/11/2015.
 */
public class SawToothFunction implements IWaveFunction {
    public static final SawToothFunction Instance = new SawToothFunction();
    @Override
    public double calculate(double index) {
        return -2.0 / Math.PI * Math.atan(1.0 / Math.tan(index * Math.PI));
    }
}
