package ModelBoard.Pieces;

import ModelBoard.Board.Board;
import ModelBoard.Direction;

/**
 * Created by Irindul on 13/03/2017.
 */
public class GravityDeomon implements Runnable {

    Board board;
    Piece piece;
    static int t;
    int i;

    public GravityDeomon(Board board, Piece piece) {
        this.board = board;
        this.piece = piece;
        i = ++t;
    }

    @Override
    public void run() {
        if(piece.onlyFalse())
            return;
        board.movePiece(Direction.DOWN, piece);
        //board.resolveHoles(piece);
    }
}
