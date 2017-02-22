package ModelBoard.Observers;

/**
 * Created by Irindul on 22/02/2017.
 */
public interface GravityListener {

    void onMovement();
    void moving();
    void onChangedNext();
    void onSweep();

}
