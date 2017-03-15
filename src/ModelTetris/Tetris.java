package ModelTetris;

import ModelBoard.Board.Board;
import ModelBoard.Direction;
import ModelBoard.Observers.GravityListener;
import ModelBoard.Pieces.Piece;
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
    private Piece current;
    private Piece next;
    public static int height = 18;
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
        board.addPiece(current);

        next = randomBlock();
                //BlockFactory.get(TetrisBlocks.Straight);
       // board.addPiece(current);



    }

    public Tetris(Tetris t){
        this.board = new Board(t.board);
        this.pieces = new ArrayList<>(7);
        for (int i = 0; i < t.pieces.size(); i++) {
            pieces.add(t.pieces.get(i));
        }
        this.current = new Piece(t.current);
        this.next = new Piece(t.next);
        this.finished = t.finished;
        this.score = t.score;
        movementListeners = new ArrayList<>();

        this.board.addPiece(current);
    }


    public void move(Direction d){
        movementListeners.forEach(GravityListener::moving);
        board.movePiece(d, current);
        movementListeners.forEach(GravityListener::onMovement);
    }

    public void applyGravity(){
        if(board.checkMovement(Direction.DOWN, current)){
            move(Direction.DOWN);

            return;
        }

        movementListeners.forEach(GravityListener::moving);
        board.resolveHoles(current);
        movementListeners.forEach(GravityListener::onMovement);


        movementListeners.forEach(GravityListener::sweeping);
        score(board.sweep());
        movementListeners.forEach(GravityListener::onSweep);

        if(!this.isFinished()){
            swapCurrent();
        } else {
            quit();
            movementListeners.forEach(GravityListener::onQuit);
        }

    }

    private void score(int i) {
        //score += (Math.exp(i)*5);
        score += i;
    }

    public void addToBoard(){
        board.addPiece(current);
    }

    public int getScore(){
        return score;
    }

    public Piece getNext(){
        return next;
    }

    public void quit(){
        board.onQuit();
    }

    public Piece randomBlock(){

        if(pieces.size() == 0){
            for (int i = 0; i < 7; i++) {
                pieces.add(i);
            }

            Collections.shuffle(pieces);
        }



        int value = pieces.get(0);
        pieces.remove(0);
        return BlockFactory.get(TetrisBlocks.values()[value]);
        //return BlockFactory.get(TetrisBlocks.LeftL);
    }

    public void rotate(){
        movementListeners.forEach(GravityListener::moving);
        board.rotateClockWise(current);
        movementListeners.forEach(GravityListener::onMovement);
    }

    public void randomRotate(Piece piece){
        Random rd = new Random();
        int numberOfRotation = rd.nextInt(4);
        for (int i = 0; i < numberOfRotation; i++) {
            board.rotateClockWise(piece);
        }
    }

    public boolean isFinished() {
       return (!board.isEmptyRow(0) || !board.isEmptyRow(1));
    }

    public Piece getCurrent(){
        return current;
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
        boolean atLeastOne;
        for (int i = 0; i < height; i++) {
            atLeastOne = false;
            for (int j = 0; j < width; j++) {
                Position tmp = new Position(i, j);
                if(!board.isEmpty(tmp)) {
                    atLeastOne = true;
                } else if (board.isEmpty(tmp) && atLeastOne){
                    holes++;
                }
            }
        }
        return holes;
    }

    private int height(int j){
        int heightC = 0;


        for (int i = height-1; i >= 0; i--) {
            Position tmp = new Position(i, j);

            if(board.isInPiece(current, tmp)){
               if(!board.isEmpty(tmp))
                    heightC = height - i; //We count the height only if this is not the current block
                //Height - i is to calculate the height as 0 is on top and height is on the bottom
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


    public void swapCurrent(){
        board.addDaeomon(current);
        current = next;
        next = randomBlock();
        addToBoard();
        randomRotate(current);
        movementListeners.forEach(GravityListener::onChangedNext);
        move(Direction.DOWN);
        move(Direction.DOWN);

    }

    public void resolve() {
        board.resolveHoles(current);
    }
}
