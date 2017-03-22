package ModelBoard.Pieces;

import ModelBoard.Board.Board;
import ModelBoard.Direction;
import ModelBoard.Observers.GravityListener;

/**
 * Created by Irindul on 13/03/2017.
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
        if(piece.onlyFalse()) {
            return;
        }

        if(board.contains(piece)){
            board.movePiece(Direction.DOWN, piece);
            listener.onSweep();
        }

    }
}
