package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private double period;
    private int state;
    private double factor;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.factor = factor;
        this.state = 0;
    }

    private double normalize(int state) {
        /*Return a state */
        return state * 2.0 / (period - 1) - 1;
    }

    @Override
    public double next() {
        if (period < 1){
            return 0;
        }
        state = (state + 1) % (int) period;
        double ret = normalize(state);
        if (state == (int) period - 1){
            state = 0;
            period = period * factor;
        }
        return ret;
    }
}
