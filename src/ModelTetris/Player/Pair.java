package ModelTetris.Player;

/**
 * Created by Irindul on 22/02/2017.
 */
public class Pair<T, K> {
    private T first;

    private K second;


    public void setFirst(T first){
        this.first = first;
    }

    public void setSecond(K second) {
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public K getSecond() {
        return second;
    }
}
