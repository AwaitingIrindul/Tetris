package ModelTetris;

import ModelBoard.Board.Board;
import ModelBoard.Board.Grid;
import ModelBoard.Direction;
import ModelBoard.Observers.GravityListener;
import ModelBoard.Pieces.Block;
import ModelBoard.Pieces.BlockAggregate;
import ModelBoard.Position.Position;

import java.util.ArrayList;
import java.util.Collections;
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
    private boolean finished;
    private int score;
    private List<Integer> pieces;


    private List<GravityListener> movementListeners;

    public Tetris() {
        board = new Board(height, width);
        pieces = new ArrayList<>(7);
        movementListeners = new ArrayList<>();
        current = randomBlock();
                 //BlockFactory.get(TetrisBlocks.RightL);

        next = randomBlock();
                //BlockFactory.get(TetrisBlocks.Straight);
       // board.addPiece(current);



    }

    public Tetris(Tetris t){
        this.board = new Board(t.board);
        this.current = new BlockAggregate(t.current);
        this.next = new BlockAggregate(t.next);
        this.finished = t.finished;
        this.score = t.score;
        //this.board.addPiece(current);
    }


    public void move(Direction d){
        board.movePiece(d, current);
    }


    public List<BlockAggregate> getBlocks(){
        return board.getBlockAggregates();
    }

    public void applyGravity(){



        
        if(board.checkMovement(Direction.DOWN, current)){
            //ADD OBSERVER MOVING NOTIFICATION
            movementListeners.forEach(GravityListener::moving);
            move(Direction.DOWN);
            movementListeners.forEach(GravityListener::onMovement);
            return;

        }

        board.addPiece(current);

        score(board.sweep());

        board.getGrid().display();
        System.out.println();
        movementListeners.forEach(GravityListener::onSweep);

        if(!this.isFinished()){

            current = next;
            next = randomBlock();
            movementListeners.forEach(GravityListener::onChangedNext);

        }

    }

    private void score(int i) {
        //score += (Math.exp(i)*5);
        score += i;
    }

    public int getScore(){
        return score;
    }

    private void applyGravityExceptCurrent() {
        for (int i = 0; i < height; i++) {
            for(BlockAggregate b : board.getBlockAggregates()){ //We make every block move down
                if(!b.equals(current))
                    board.movePiece(Direction.DOWN, b);
            }
        }

    }

    public BlockAggregate getNext(){
        return next;
    }

    public BlockAggregate randomBlock(){

        if(pieces.size() == 0){
            for (int i = 0; i < 7; i++) {
                pieces.add(i);
            }

            Collections.shuffle(pieces);
        }



        int value = pieces.get(0);
        pieces.remove(0);
        return BlockFactory.get(TetrisBlocks.values()[value]);
        //return BlockFactory.get(TetrisBlocks.Straight);
    }

    public void rotate(){
        board.rotateClockWise(current);
    }

    public void randomRotate(BlockAggregate blockAggregate  ){
        Random rd = new Random();
        int numberOfRotation = rd.nextInt(4);
        for (int i = 0; i < numberOfRotation; i++) {
            board.rotateClockWise(blockAggregate);
        }
    }

    public boolean isFinished() {
       return !board.isEmptyRow(0);
    }

    public BlockAggregate getCurrent(){
        return current;
    }

    public Grid getGrid(){
        return board.getGrid();
    }

    public int sumHeight() {
        int max = 0;
        for (int i = 0; i < width; i++) {
            max += height(i);
        }

        return max;
    }

    public int rowsToSweep(){
        return board.rowsToSweep();
    }


    public int holes() {
        int holes = 0;
        boolean up = false;
        boolean atLeastOne =false;
        for (int i = 0; i < width; i++) {
            for (int j = height - 1; j >= 0; j--) {

                if(atLeastOne){
                    Position tmp = Direction.UP.getNewPosition(new Position(j, i));
                    int upX = tmp.getX();
                    int upY = tmp.getY();
                    //We check if there is a block on top of the current pos
                    if(board.getGrid().isInRange(upX, upY)){
                        if(board.getGrid().isEmpty(j, i))
                            up = ! board.getGrid().isEmpty(upX, upY);
                        else up = false;
                    }

                    if(up){ //If there is something on top of this cell, there is a hole
                        holes++;
                    }
                } else {
                    if(!board.getGrid().isEmpty(j, i)){
                        atLeastOne = true;
                    }
                }

            }
        }
        // TODO: 20/02/2017 VERIFY
        return holes;
    }

    private int height(int j){
        int heightC = 0;
        boolean currentOnTheGround = false;


        //We check if the current block is on the ground
        for (Block block : current.getBlocks()){
            for (int k = 0; k < block.getHeight(); k++) {
                for (int l = 0; l < block.getWidth(); l++) {
                    Position down = Direction.DOWN.getNewPosition(block.getPosition(k, l));

                    //If at least on of the block has a block below it or is not in the grid then the block is on the ground
                    if(board.getGrid().isInRange(down.getX(), down.getY())){
                        if(!board.getGrid().isEmpty(down.getX(), down.getY())){
                            currentOnTheGround = true;
                        }
                    } else {
                        currentOnTheGround = true;
                    }

                }
            }
        }



        for (int i = height-1; i >= 0; i--) {
            Position tmp = new Position(i, j);
            if(!current.isInBlock(tmp)){
                if(!board.getGrid().isEmpty(i, j))
                    heightC = height - i; //We count the height only if this is not the current block
                //Height - i is to calculate the height as 0 is on top and height is on the bottom

            } else {
                if(currentOnTheGround){
                    if(!board.getGrid().isEmpty(i, j))
                    heightC = height - i;
                }
            }

        }
        return heightC;
    }

    public int bumpiness() {
        int bumpiness = 0;

        for (int i = 0; i < width - 1; i++) {
            bumpiness += Math.abs(height(i) - height(i+1));
        }

        return bumpiness;

    }


    public void addGravityListener(GravityListener listener){
        movementListeners.add(listener);
    }

    // TODO: 22/02/2017 Design pattern observer observable pour positions et score

}
