package ModelTetris;

import ModelBoard.Board.Board;
import ModelBoard.Board.Grid;
import ModelBoard.Direction;
import ModelBoard.Pieces.Block;
import ModelBoard.Pieces.BlockAgregat;
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


        BlockAgregat blocks = new BlockAgregat();
        Block block = new Block(1, 1);
        block.setPosition(new Position(5, 5));
        Block block1 = new Block(1, 1);
        block1.setPosition(new Position(5, 6));
        blocks.add(block);
        blocks.add(block1);
        board.addPiece(blocks);


        while (board.checkMovement(Direction.DOWN, 0))
          board.movePiece(Direction.DOWN, 0);

    }

    public void display(){
        Grid grid = board.getGrid();
        boolean blocking = false;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                blocking = false;
                for(BlockAgregat blocks : board.getBlockAgregats()){
                    for(Block block : blocks.getBlocks()){
                        if(block.getPosition().equals(new Position(i, j))){
                            blocking = true;
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