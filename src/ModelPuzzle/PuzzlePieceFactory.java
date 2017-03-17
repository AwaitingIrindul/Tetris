package ModelPuzzle;

import ModelBoard.Pieces.Piece;
import ModelBoard.Position.Position;

/**
 * Created by Irindul on 15/03/2017.
 */
public class PuzzlePieceFactory {



    public static Piece get(PuzzlePieces p){
        switch (p){

            case Two:
                return getTwo();
            case Three:
                return getThree();
            default:
                return getTwo();
        }
    }

    private static Piece getTwo() {
        String[][] scheme = {
                {"1"},
                {"1"}
        };

        return new Piece(scheme, new Position(0, 0));
    }

    private static Piece getThree() {
        String[][] scheme = {
                {"1"},
                {"1"},
                {"1"}
        };

        return new Piece(scheme, new Position(0, 0));
    }
}
