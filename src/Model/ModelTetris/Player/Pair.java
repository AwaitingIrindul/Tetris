package Model.ModelTetris.Player;

/**
 * Created by Irindul on 22/02/2017.
 * Custom pair class with generics
 */
class Pair<T, K> {
    private T first;

    private K second;


    void setFirst(T first){
        this.first = first;
    }

    void setSecond(K second) {
        this.second = second;
    }

    T getFirst() {
        return first;
    }

    K getSecond() {
        return second;
    }
}
