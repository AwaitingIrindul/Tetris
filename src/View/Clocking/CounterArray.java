package View.Clocking;

/**
 *
 * Created by Mathieu on 28/09/2016.
 */
public class CounterArray {
    private Counter[] counters;

    public CounterArray(int size, int[] steps, int[] infs, int[] sups) {
        counters = new Counter[size];
        for(int i = 0;i < size; i++){
            counters[i] = new Counter(steps[i], infs[i], sups[i]);
        }
    }

    public void increment(){ // Wrapper so that the user just have to call increment().
        this.increment(0);

    }

    private void increment(int n){  //Recursive function.
        if( n == counters.length-1){ //Stop case : If the  n is the last element of the array.
            if(counters[n].getValue() < counters[n].getSup()-1){ //If we can still increment the value
                counters[n].increment();
            } else {
                this.flush(); //If we are at the maximum value, we flush everything.
            }
        } else {
            if(counters[n].getValue() < counters[n].getSup()-1){ //Same with the other element of the array.
                counters[n].increment();
            } else {
                counters[n].increment();
                this.increment(n+1); //We call again the function with the next element of the array.
            }
        }
    }

    public void flush(){
        for (Counter counter : counters) {
            counter.flush(); //Reset every element of the array to the minimum
        }
    }

    public Counter getCounterI(int i){
        return counters[i];
    }

    public int getLength(){
        return counters.length;
    }
}