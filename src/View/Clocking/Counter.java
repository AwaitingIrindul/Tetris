package View.Clocking;

/**
 *
 * Created by Irindul on 26/09/2016.
 * Class of a general counter. It contains a value, a step and other stuffs.
 */
public class Counter {
    private int step;
    private int inf;
    private int sup;
    private int value;

    /**
     * Constructor of the class by default.
     */


    Counter(int step, int inf, int sup) {
        this.step = step;
        this.inf = inf;
        this.sup = sup;
        this.value = this.inf;
    }

    /**
     * The function increment the value of the Counter.
     * If the value is superior to the max, we reset it to the minimum
     */
    void increment(){ /*When we add something in our counter, we call this fonction*/
        if(this.value + this.step < this.sup) /*If the value + the step does not go over the sup bound*/
            this.value += this.step; /*We increment*/
        else this.flush(); /*Otherwise we loop over*/
    }

    /**
     * The function decrement the value of the Counter.
     * If the value is inferior to the min, we let it to the minimum
     */
    public void decrement(){ /*When we delete something in our counter, we call this function*/
        if(this.value - this.step > this.inf) /*If the value - the step does not go under the inf bound*/
            this.value -= this.step; /*We increment*/
        else this.flush(); /*Otherwise we stay at the minimum*/
    }

    void flush(){
        this.value = this.inf; /*Reset the counter to the minimum*/
    }

    public int getStep() {
        return step;
    }


    public int getInf() {
        return inf;
    }

    int getSup() {
        return sup;
    }

    public int getValue() {
        return value;
    }

}