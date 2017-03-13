package ModelBoard.Pieces;

import ModelBoard.Board.Board;
import ModelBoard.Direction;
import ModelBoard.Observers.GravityListener;

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
            if(board.checkMovement(Direction.DOWN, piece)){
                board.movePiece(Direction.DOWN, piece);
            }
            System.out.println("Checking : " + i);
            
            board.resolveHoles(piece);

    }
}
