package ModelPuzzle;

import ModelBoard.Board.Board;
import ModelBoard.Direction;
import ModelBoard.Observers.GravityListener;
import ModelBoard.Pieces.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irindul on 15/03/2017.
 */
public class Puzzle {

    private Board board;
    private Piece goal;
    public static int height = 6;
    public static int width = 6;
    private List<Piece> pieces;
    private Piece selected;


    public Puzzle() {
        this.board = new Board(height, width);
        goal = PuzzlePieceFactory.get(PuzzlePieces.TwoH);


        pieces = new ArrayList<>();
        this.addPiece(PuzzlePieceFactory.get(PuzzlePieces.ThreeH));
        board.addPiece(pieces.get(0));
        selected = pieces.get(0);
        moveSelected(Direction.RIGHT);
        moveSelected(Direction.RIGHT);
        board.addPiece(goal);
    }

    public void addPiece(Piece piece){
        pieces.add(piece);
        board.addPiece(piece);
    }

    public void setSelected(Piece piece){
        selected = piece;
    }

    public void moveSelected(Direction direction){
        if (selected != null) {
            board.movePiece(direction, selected);
        }
    }

    public boolean checkSelected(Direction direction){
        if (selected != null) {
            return board.checkMovement(direction, selected);
        }

        return false;
    }

    public Piece getGoal(){
        return goal;
    }

    public List<Piece> getPieces(){
        return pieces;
    }
}
