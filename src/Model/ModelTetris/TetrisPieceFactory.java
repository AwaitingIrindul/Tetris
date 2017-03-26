package Model.ModelTetris;

import Model.ModelBoard.Orientation;
import Model.ModelBoard.Pieces.Piece;
import Model.ModelBoard.Position.Position;

/**
 * Created by Irindul on 10/02/2017.
 * Tetris piece factory
 */
class TetrisPieceFactory {

    private static Position startingPosition = new Position(0, 0);
    private static Orientation orientation = Orientation.HORIZONTAL;

    static Piece get(TetrisBlocks t){
        switch (t){

            case Straight:
                return getStraight();
            case RightL:
                return getRightL();
                
            case LeftL:
                return getLeftL();
                
            case TwoByTwo:
                return getTwoByTwo();
                
            case RightZ:
                return getRightZ();
                
            case LeftZ:
                return getLeftZ();
                
            case ThreeOne:
                return getThreeOne();
                
            default:
                return null;

        }
        
        
    }

    private static Piece getStraight(){

        String[][] scheme = {
                {"1"},
                {"1"},
                {"1"},
                {"1"}
        };


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return new Piece(scheme, startingPosition, orientation);

    }

    private static Piece getLeftL(){
        String[][] scheme = {
                {"0", "1"},
                {"0", "1"},
                {"1", "1"},
        };


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return new Piece(scheme, startingPosition, orientation);
    }

    private static Piece getRightL(){
        String[][] scheme = {
                {"1", "0"},
                {"1", "0"},
                {"1", "1"},
        };


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return new Piece(scheme, startingPosition, orientation);
    }

    private static Piece getTwoByTwo(){
        String[][] scheme = {
                {"1", "1"},
                {"1", "1"}
        };


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return new Piece(scheme, startingPosition, orientation);
    }

    private static Piece getRightZ(){
        String[][] scheme = {
                {"1", "0"},
                {"1", "1"},
                {"0", "1"}
        };


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return new Piece(scheme, startingPosition, orientation);
    }

    private static Piece getLeftZ(){
        String[][] scheme = {
                {"0", "1"},
                {"1", "1"},
                {"1", "0"}
        };


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return new Piece(scheme, startingPosition, orientation);
    }

    private static Piece getThreeOne(){
        String[][] scheme = {
                {"0", "1", "0"},
                {"1", "1", "1"},
        };


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return new Piece(scheme, startingPosition, orientation);

    }

}
