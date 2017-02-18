package ModelTetris;

import ModelBoard.Board.Board;
import ModelBoard.Board.Grid;
import ModelBoard.Direction;
import ModelBoard.Pieces.Block;
import ModelBoard.Pieces.BlockAggregate;
import ModelBoard.Position.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Tetris {
    private Board board;
    private BlockAggregate current;
    private BlockAggregate next;
    public static int height = 16;
    public static int width = 10;
    public boolean finished;

    public Tetris() {
        board = new Board(height, width);

        current = randomBlock();

        next = randomBlock();
                //BlockFactory.get(TetrisBlocks.LeftZ);
        board.addPiece(current);

    }

    public void move(Direction d){
        board.movePiece(d, board.getIndex(current));
    }


    public List<BlockAggregate> getBlocks(){
        return board.getBlockAggregates();
    }

    public boolean applyGravity(){
        List<Position[][]> positions = new ArrayList<>();
        for(Block b : current.getBlocks()){
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
                        hasMoved = false; //If the positions are the same, then the block didn't move
                    } else {
                        hasMoved = true; //Otherwise if one block moved, we stop the loop
                        break;
                    }
                }
            }
            k++;
        }

        if(!hasMoved){ //If our block hasn't moved, then it's blocked
            board.sweep();

            current = next; //We change the new current
            next = randomBlock();

            if(! board.addPiece(current)) //We add this new piece on the board.
                finished = true;
            randomRotate(current);
            return true;
        }
        return  false;
    }

    public BlockAggregate getNext(){
        return next;
    }

    public BlockAggregate randomBlock(){
        Random rd = new Random();
        int value;
        value  = rd.nextInt(TetrisBlocks.values().length);

        return BlockFactory.get(TetrisBlocks.values()[value]);
    }

    public void rotate(){
        board.rotateClockWise(board.getIndex(current));
    }

    public void randomRotate(BlockAggregate blockAggregate  ){
        Random rd = new Random();
        int numberOfRotation = rd.nextInt(4);
        for (int i = 0; i < numberOfRotation; i++) {
            board.rotateClockWise(board.getIndex(blockAggregate));
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
