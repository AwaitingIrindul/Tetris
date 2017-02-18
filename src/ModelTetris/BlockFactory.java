package ModelTetris;

import ModelBoard.Direction;
import ModelBoard.Pieces.Block;
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

        Block block = new Block(1 ,1);
        block.setPosition(new Position(startingPosition.getX(), startingPosition.getY()));

        Block block2 = new Block(1, 1);
        Block block3 = new Block(1, 1);
        Block block4 = new Block(1, 1);
        BlockAggregate blockAggregate = new BlockAggregate();
        blockAggregate.add(block);

        blockAggregate.add(block2, block, new Position(0, 0), Direction.RIGHT);
        blockAggregate.add(block3, block2, new Position(0, 0), Direction.RIGHT);
        blockAggregate.add(block4, block3, new Position(0, 0), Direction.RIGHT);

        blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return blockAggregate;

    }

    private static BlockAggregate getLeftL(){
        Block block = new Block(1, 1);
        block.setPosition(new Position(startingPosition.getX(), startingPosition.getY()));

        Block block2 = new Block(1,1);
        Block block3 = new Block(1 ,1);

        BlockAggregate blockAggregate = new BlockAggregate();
        blockAggregate.add(block);

        Block block4 = new Block(1, 1);

        blockAggregate.add(block2, block, new Position(0, 0), Direction.DOWN);
        blockAggregate.add(block3, block2, new Position(0, 0), Direction.DOWN);
        blockAggregate.add(block4, block3, new Position(0, 0), Direction.LEFT);
        blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return blockAggregate;
    }

    private static BlockAggregate getRightL(){
        Block block = new Block(1, 1);
        block.setPosition(new Position(startingPosition.getX(), startingPosition.getY()));

        Block block2 = new Block(1,1);
        Block block3 = new Block(1 ,1);

        BlockAggregate blockAggregate = new BlockAggregate();
        blockAggregate.add(block);

        Block block4 = new Block(1, 1);

        blockAggregate.add(block2, block, new Position(0, 0), Direction.DOWN);
        blockAggregate.add(block3, block2, new Position(0, 0), Direction.DOWN);
        blockAggregate.add(block4, block3, new Position(0, 0), Direction.RIGHT);
        blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return blockAggregate;
    }

    private static BlockAggregate getTwoByTwo(){
        Block block = new Block(1, 1);
        block.setPosition(new Position(startingPosition.getX(), startingPosition.getY()));

        Block block2 = new Block(1, 1);
        Block block3 = new Block(1, 1);
        Block block4 = new Block(1, 1);
        BlockAggregate blockAggregate = new BlockAggregate();
        blockAggregate.add(block);
        blockAggregate.add(block2, block, new Position(0, 0), Direction.RIGHT);
        blockAggregate.add(block3, block2, new Position(0, 0), Direction.DOWN);
        blockAggregate.add(block4, block3, new Position(0, 0), Direction.LEFT);
        blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return blockAggregate;
    }

    private static BlockAggregate getRightZ(){
        Block block = new Block(1 ,1);
        block.setPosition(new Position(startingPosition.getX(), startingPosition.getY()));

        Block block2 = new Block(1, 1);
        Block block3 = new Block(1, 1);
        Block block4 = new Block(1, 1);
        BlockAggregate blockAggregate = new BlockAggregate();
        blockAggregate.add(block);
        blockAggregate.add(block2, block, new Position(0, 0), Direction.DOWN);
        blockAggregate.add(block3, block2, new Position(0, 0), Direction.RIGHT);
        blockAggregate.add(block4, block3, new Position(0, 0), Direction.DOWN);
        blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return blockAggregate;
    }

    private static BlockAggregate getLeftZ(){
        Block block = new Block(1 ,1);
        block.setPosition(new Position(startingPosition.getX(), startingPosition.getY()));

        Block block2 = new Block(1, 1);
        Block block3 = new Block(1, 1);
        Block block4 = new Block(1, 1);
        BlockAggregate blockAggregate = new BlockAggregate();
        blockAggregate.add(block);
        blockAggregate.add(block2, block, new Position(0, 0), Direction.DOWN);
        blockAggregate.add(block3, block2, new Position(0, 0), Direction.LEFT);
        blockAggregate.add(block4, block3, new Position(0, 0), Direction.DOWN);
        blockAggregate.setOrigin(new Origin(block2, new Position(0, 0)));

        return blockAggregate;
    }

    private static BlockAggregate getThreeOne(){
        Block block = new Block(1,1);
        block.setPosition(new Position(startingPosition.getX(), startingPosition.getY()));

        Block block2 = new Block(1, 1);
        Block block3 = new Block(1, 1);
        Block block4 = new Block(1, 1);

        BlockAggregate blockAggregate = new BlockAggregate();
        blockAggregate.add(block);
        blockAggregate.add(block2, block, new Position(0, 0), Direction.RIGHT);
        blockAggregate.add(block3, block2, new Position(0, 0), Direction.RIGHT);
        blockAggregate.add(block4, block2, new Position(0, 0), Direction.DOWN);
        blockAggregate.setOrigin(new Origin(block4, new Position(0, 0)));

        return blockAggregate;

    }

}
