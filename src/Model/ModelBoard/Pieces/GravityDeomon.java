package Model.ModelBoard.Pieces;

import Model.ModelBoard.Board.Board;
import Model.ModelBoard.Direction;
import Model.ModelBoard.Observers.GravityListener;

/**
 * Created by Irindul on 13/03/2017.
 */
public class GravityDeomon implements Runnable {

    Board board;
    Piece piece;
    GravityListener listener;
    int i;

    public GravityDeomon(Board board, Piece piece, GravityListener listener) {
        this.board = board;
        this.piece = piece;
        this.listener = listener;
        i = 0;
    }

    @Override
    public void run() {
        if(piece.onlyFalse())
        {
            return;
        }

        if(board.contains(piece)){
            board.movePiece(Direction.DOWN, piece);
            listener.onSweep();
        }

    }
}
