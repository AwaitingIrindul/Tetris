package Model.ModelBoard.Observers;

import Model.ModelBoard.Pieces.Piece;

/**
 * Created by Irindul on 22/02/2017.
 * Interface to implement Observer design pattern
 */
public interface GravityListener {

    void onMovement();
    void onChangedNext();
    void onSweep();
    void onQuit();
    void onCleanUp(Piece p);
    void update(Piece p);
}
