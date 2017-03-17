package ModelTetris;

import ModelBoard.Pieces.Piece;
import ModelBoard.Position.Position;

/**
 * Created by Irindul on 10/02/2017.
 */
public class TetrisPieceFactory {

    public static Position startingPosition = new Position(0, 4);
    
    public static Piece get(TetrisBlocks t){
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

        Piece piece = new Piece(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return piece;

    }

    private static Piece getLeftL(){
        String[][] scheme = {
                {"0", "1"},
                {"0", "1"},
                {"1", "1"},
        };

        Piece piece = new Piece(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return piece;
    }

    private static Piece getRightL(){
        String[][] scheme = {
                {"1", "0"},
                {"1", "0"},
                {"1", "1"},
        };

        Piece piece = new Piece(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return piece;
    }

    private static Piece getTwoByTwo(){
        String[][] scheme = {
                {"1", "1"},
                {"1", "1"}
        };

        Piece piece = new Piece(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return piece;
    }

    private static Piece getRightZ(){
        String[][] scheme = {
                {"1", "0"},
                {"1", "1"},
                {"0", "1"}
        };

        Piece piece = new Piece(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return piece;
    }

    private static Piece getLeftZ(){
        String[][] scheme = {
                {"0", "1"},
                {"1", "1"},
                {"1", "0"}
        };

        Piece piece = new Piece(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return piece;
    }

    private static Piece getThreeOne(){
        String[][] scheme = {
                {"0", "1", "0"},
                {"1", "1", "1"},
        };

        Piece piece = new Piece(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return piece;

    }

}
