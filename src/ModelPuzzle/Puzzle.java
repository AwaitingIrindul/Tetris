package ModelPuzzle;

import ModelBoard.Board.Board;
import ModelBoard.Pieces.Piece;

/**
 * Created by Irindul on 15/03/2017.
 */
public class Puzzle {

    private Board board;
    private Piece goal;
    public static int height = 6;
    public static int width = 6;


    public Puzzle() {
        this.board = new Board(height, width);
        goal = PuzzlePieceFactory.get(PuzzlePieces.Two);
        goal.rotateClockWise();
        board.addPiece(goal);
    }


    public Piece getGoal(){
        return goal;
    }
}
