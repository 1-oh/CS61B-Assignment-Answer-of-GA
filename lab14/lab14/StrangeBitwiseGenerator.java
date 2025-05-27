package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        this.state = 0;
    }

    private double normalize(int state) {
        /*Return a state */
        return state * 2.0 / (period - 1) - 1;
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        int weirdState = state & (state >>> 3) % period;
        //int weirdState = state & (state >> 3) & (state >> 8) % period;
        return normalize(weirdState);
    }
}
