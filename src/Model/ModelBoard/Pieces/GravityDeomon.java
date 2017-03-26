package Model.ModelBoard.Pieces;

import Model.ModelBoard.Board.Board;
import Model.ModelBoard.Direction;
import Model.ModelBoard.Observers.GravityListener;

/**
 * Created by Irindul on 13/03/2017.
 * Class to apply gravity on each piece
 */
public class GravityDeomon implements Runnable {

    private Board board;
    private Piece piece;
    private GravityListener listener;

    public GravityDeomon(Board board, Piece piece, GravityListener listener) {
        this.board = board;
        this.piece = piece;
        this.listener = listener;
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
