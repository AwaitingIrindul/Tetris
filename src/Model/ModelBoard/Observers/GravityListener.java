package Model.ModelBoard.Observers;

import Model.ModelBoard.Pieces.Piece;

/**
 * Created by Irindul on 22/02/2017.
 */
public interface GravityListener {

    void onMovement();
    void onChangedNext();
    void onSweep();
    void onQuit();

    void onMovement(Piece p);
    void onCleanUp(Piece p);
}
