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


        board.addPiece(BlockFactory.get(TetrisBlocks.LeftL));
        board.movePiece(Direction.DOWN, 0);
        board.movePiece(Direction.DOWN, 0);
        board.movePiece(Direction.DOWN, 0);
        board.rotateClockWise(0);

        /*while (board.checkMovement(Direction.DOWN, 0))
          board.movePiece(Direction.DOWN, 0);*/

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