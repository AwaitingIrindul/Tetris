package ModelPuzzle;

import ModelBoard.Board.Board;
import ModelBoard.Direction;
import ModelBoard.Observers.GravityListener;
import ModelBoard.Pieces.Piece;
import ModelBoard.Position.Position;

import java.io.File;
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
    private static Position winningPosition = new Position(2, 5);

    public Puzzle() {
        this.board = new Board(height, width);
        goal = PuzzlePieceFactory.get(PuzzlePieces.TwoH);
        goal.setPosition(new Position(2, 0));

        pieces = new ArrayList<>();
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.ThreeH));
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.ThreeH));
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.ThreeV));
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.ThreeV));
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.TwoV));
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.TwoV));
        pieces.add(PuzzlePieceFactory.get(PuzzlePieces.TwoH));

        pieces.get(1).setPosition(new Position(5, 0));
        pieces.get(2).setPosition(new Position(1, 2));
        pieces.get(3).setPosition(new Position(0, 5));
        pieces.get(4).setPosition(new Position(3, 0));
        pieces.get(5).setPosition(new Position(4, 4));
        pieces.get(6).setPosition(new Position(3, 4));


        pieces.forEach(piece -> board.addPiece(piece));
        board.addPiece(getGoal());

        board.toFile("ressources/init/puzzle1.json");


        // TODO: 25/03/2017 READ AND WRITE FILES
       /* File f = new File("ressources/init/test.json");
        if(f.exists() && !f.isDirectory()) {

            
        }*/
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
            boolean ok = false;
            switch (selected.getOrientation()){

                case HORIZONTAL:
                    if(direction.equals(Direction.LEFT) || direction.equals(Direction.RIGHT))
                        ok = true;
                    break;
                case VERTICAL:
                    if(direction.equals(Direction.UP) || direction.equals(Direction.DOWN))
                        ok = true;
                    break;
                default:
                    ok = false;
            }
            if(ok)
                board.movePiece(direction, selected);


            checkFinish();
        }
    }

    private void checkFinish() {
        boolean finnish = false;

       for(Position pos : goal.getPositions()){
           if(pos.equals(winningPosition)){
               finnish =true;
               break;
           }
       }

       if(finnish){
           System.out.println("hurray");
           // TODO: 25/03/2017 Notify view

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
