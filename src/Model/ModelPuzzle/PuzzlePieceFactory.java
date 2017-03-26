package Model.ModelPuzzle;

import Model.ModelBoard.Orientation;
import Model.ModelBoard.Pieces.Piece;
import Model.ModelBoard.Position.Position;

/**
 * Created by Irindul on 15/03/2017.
 * Contains Piece definition for the puzzle game
 */
class PuzzlePieceFactory {




    static Piece get(PuzzlePieces p){
        switch (p){

            case TwoH:
                return getTwoHorizontal();
            case ThreeH:
                return getThreeHorizontal();
            case TwoV:
                return getTwoVertical();
            case ThreeV:
                return getThreeVertical();
            default:
                return getTwoHorizontal();
        }
    }

    private static Piece getTwoHorizontal() {
        String[][] scheme = {
                {"1", "1"}
        };

        return new Piece(scheme, new Position(0, 0), Orientation.HORIZONTAL);
    }

    private static Piece getTwoVertical() {
        String[][] scheme = {
                {"1"},
                {"1"}
        };

        return new Piece(scheme, new Position(0, 0), Orientation.VERTICAL);
    }

    private static Piece getThreeVertical() {
        String[][] scheme = {
                {"1"},
                {"1"},
                {"1"}
        };

        return new Piece(scheme, new Position(0, 0), Orientation.VERTICAL);
    }

    private static Piece getThreeHorizontal() {
        String[][] scheme = {
                {"1", "1", "1"},
        };

        return new Piece(scheme, new Position(0, 0), Orientation.HORIZONTAL);
    }
}
