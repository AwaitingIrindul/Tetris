package ModelTetris;

import ModelBoard.Direction;
import ModelBoard.Pieces.BlockAggregate;
import ModelBoard.Pieces.Origin;
import ModelBoard.Position.Position;

/**
 * Created by Irindul on 10/02/2017.
 */
public class BlockFactory {

    public static Position startingPosition = new Position(0, 4);
    
    public static BlockAggregate get(TetrisBlocks t){
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

    private static BlockAggregate getStraight(){

        String[][] scheme = {
                {"1"},
                {"1"},
                {"1"},
                {"1"}
        };

        BlockAggregate blockAggregate = new BlockAggregate(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return blockAggregate;

    }

    private static BlockAggregate getLeftL(){
        String[][] scheme = {
                {"0", "0", "1", "0"},
                {"0", "0", "1", "0"},
                {"0", "1", "1", "0"},
                {"0", "0", "0", "0"}
        };

        BlockAggregate blockAggregate = new BlockAggregate(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        //TODO change with real one
        return blockAggregate;
    }

    private static BlockAggregate getRightL(){
        String[][] scheme = {
                {"1", "0"},
                {"1", "0"},
                {"1", "1"},
        };

        BlockAggregate blockAggregate = new BlockAggregate(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        //TODO change with real one
        return blockAggregate;
    }

    private static BlockAggregate getTwoByTwo(){
        String[][] scheme = {
                {"1", "1"},
                {"1", "1"}
        };

        BlockAggregate blockAggregate = new BlockAggregate(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        //TODO change with real one
        return blockAggregate;
    }

    private static BlockAggregate getRightZ(){
        String[][] scheme = {
                {"1", "0"},
                {"1", "1"},
                {"0", "1"}
        };

        BlockAggregate blockAggregate = new BlockAggregate(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        //TODO change with real one
        return blockAggregate;
    }

    private static BlockAggregate getLeftZ(){
        String[][] scheme = {
                {"0", "1"},
                {"1", "1"},
                {"1", "0"}
        };

        BlockAggregate blockAggregate = new BlockAggregate(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        //TODO change with real one
        return blockAggregate;
    }

    private static BlockAggregate getThreeOne(){
        String[][] scheme = {
                {"0", "1", "0"},
                {"1", "1", "1"},
        };

        BlockAggregate blockAggregate = new BlockAggregate(scheme, startingPosition);


        //blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        //TODO change with real one
        return blockAggregate;

    }

}
