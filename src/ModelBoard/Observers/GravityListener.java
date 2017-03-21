package ModelBoard.Observers;

/**
 * Created by Irindul on 22/02/2017.
 */
public interface GravityListener {

    //// TODO: 21/03/2017 Create inheritance
    void onMovement();
    void onChangedNext();
    void onSweep();
    void onQuit();
}
