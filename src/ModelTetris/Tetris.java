package ModelTetris;

import ModelBoard.Board.Board;
import ModelBoard.Board.Grid;
import ModelBoard.Direction;
import ModelBoard.Pieces.Block;
import ModelBoard.Pieces.BlockAggregate;
import ModelBoard.Position.Position;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Tetris {
    private Board board;
    public static int height = 16;
    public static int width = 10;

    public Tetris() {
        board = new Board(height, width);


        BlockAggregate blocks = new BlockAggregate();
        Block block = new Block(2, 1);
        block.setPosition(new Position(5, 5));
        Block block1 = new Block(1, 1);
        blocks.add(block);
        blocks.add(block1, new Position(1, 0), Direction.RIGHT);


        Block block2 = new Block(2, 1);
        blocks.add(block2, new Position(0, 0), Direction.RIGHT);
        board.addPiece(blocks);


        while (board.checkMovement(Direction.RIGHT, 0))
          board.movePiece(Direction.RIGHT, 0);

    }

    public void display(){
        Grid grid = board.getGrid();
        boolean blocking = false;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                blocking = false;
                for(BlockAggregate blocks : board.getBlockAggregates()){
                    for(Block block : blocks.getBlocks()){
                        for (int k = 0; k < block.getHeight(); k++) {
                            for (int l = 0; l < block.getWidth(); l++) {
                                if(block.getPosition(k, l).equals(new Position(i, j))){
                                    blocking = true;
                                }
                            }
                        }

                    }
                }


                if(blocking){
                    System.out.print("x");
                } else {
                    System.out.print("_");
                }
                System.out.print(" ");
            }
            System.out.println();

        }
    }
}
//TODO Do a piece factory for tetris