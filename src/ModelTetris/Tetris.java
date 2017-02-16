package ModelTetris;

import ModelBoard.Board.Board;
import ModelBoard.Board.Grid;
import ModelBoard.Direction;
import ModelBoard.Pieces.Block;
import ModelBoard.Pieces.BlockAggregate;
import ModelBoard.Position.Position;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Tetris {
    private Board board;
    private BlockAggregate current;
    public static int height = 16;
    public static int width = 10;

    public Tetris() {
        board = new Board(height, width);

        current = randomBlock();
        board.addPiece(current);

    }

    public void move(Direction d){
        board.movePiece(d, board.getIndex(current));
    }


    public List<BlockAggregate> getBlocks(){
        return board.getBlockAggregates();
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


    public void applyGravity(){
        List<Position[][]> positions = new ArrayList<>();
        for(Block b : current.getBlocks()){ // TODO: 16/02/2017 Clean this up 
            Position[][] tmp = new Position[b.getHeight()][b.getWidth()];
            
            //We store each position of the current object
            for (int i = 0; i < b.getHeight(); i++) {
                for (int j = 0; j < b.getWidth(); j++) {
                    tmp[i][j] = new Position(0, 0);
                    tmp[i][j].setXY(b.getPosition(i, j).getX(), b.getPosition(i, j).getY());
                }
            }
            positions.add(tmp);
        }

        for(BlockAggregate b : board.getBlockAggregates()){ //We make every block move down
            board.movePiece(Direction.DOWN, board.getIndex(b));
        }

        boolean hasMoved = true;
        int k = 0;
        for(Block b: current.getBlocks()){ //We check if the position store match the new current position
            for (int i = 0; i < b.getHeight(); i++) {
                for (int j = 0; j < b.getWidth(); j++) {
                    if(b.getPosition(i, j).equals(positions.get(k)[i][j])){
                        hasMoved = false; //If the position are the same, then the block didn't move
                    } else {
                        hasMoved = true; //Otherwise if one block moved, we stop the loop
                        break;
                    }
                }
            }
            k++;
        }

        if(!hasMoved){ //If our block hasn't moved, then it's blocked

            // TODO: 16/02/2017 refactor in method + refactor with next block
            current = randomBlock(); //We change the new current
            board.addPiece(current); //We add this new piece on the board. 
        }
    }

    public BlockAggregate randomBlock(){
        Random rd = new Random();
        int value;
        value  = rd.nextInt(TetrisBlocks.values().length);


        return BlockFactory.get(TetrisBlocks.values()[value]);
    }

    @Deprecated
    public Grid getGrid(){
        return board.getGrid();
    }
}


//TODO Store current moving piece and next piece