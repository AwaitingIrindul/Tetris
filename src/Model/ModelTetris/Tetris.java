package Model.ModelTetris;

import Model.ModelBoard.Board.Board;
import Model.ModelBoard.Direction;
import Model.ModelBoard.Observers.GravityListener;
import Model.ModelBoard.Pieces.Piece;
import Model.ModelBoard.Position.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;

/**
 * Created by Irindul on 09/02/2017.
 * Contains Tetris rules
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
    private static Position position = new Position(0, 4);

    private List<GravityListener> movementListeners;

    public Tetris() {
        board = new Board(height, width);
        pieces = new ArrayList<>(7);
        movementListeners = new ArrayList<>();
        current = randomBlock();
        current.setPosition(position);
        board.addPiece(current);

        next = randomBlock();
                //BlockFactory.get(TetrisBlocks.Straight);
       // board.addPiece(current);



    }


    public Tetris(Tetris t){
        this.board = new Board(t.board);
        this.pieces = new ArrayList<>(7);
        pieces.addAll(t.pieces);
        this.current = new Piece(t.current);
        this.next = new Piece(t.next);
        this.finished = t.finished;
        this.score = t.score;
        movementListeners = new ArrayList<>();

        this.board.addPiece(current);
    }


    public void move(Direction d){
        board.movePiece(d, current);
        movementListeners.forEach(GravityListener::onMovement);
    }

    @Override
    public String toString() {
        return board.toString();
    }

    public void applyGravity(){
        if(board.checkMovement(Direction.DOWN, current)){
            move(Direction.DOWN);

            return;
        }

        movementListeners.forEach(GravityListener::onMovement);


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

    private void addToBoard(){
        board.addPiece(current);
    }

    public int getScore(){
        return score;
    }

    public Piece getNext(){
        return next;
    }

    public void quit(){
        stop();
        board.onQuit();
    }

    private Piece randomBlock(){

        if(pieces.size() == 0){
            for (int i = 0; i < 7; i++) {
                pieces.add(i);
            }

            Collections.shuffle(pieces);
        }



        int value = pieces.get(0);
        pieces.remove(0);
        return TetrisPieceFactory.get(TetrisBlocks.values()[value]);
    }

    public void rotate(){
        board.rotateClockWise(current);
        movementListeners.forEach(GravityListener::onMovement);
    }

    private void randomRotate(Piece piece){
        Random rd = new Random();
        int numberOfRotation = rd.nextInt(4);
        for (int i = 0; i < numberOfRotation; i++) {
            board.rotateClockWise(piece);
        }
    }

    private boolean isFinished() {
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
        board.addListener(listener);
    }


    private void swapCurrent(){
        board.addDaeomon(current, movementListeners.get(0));
        movementListeners.forEach(gravityListener -> gravityListener.update(getCurrent()));
        current = next;
        current.setPosition(position);
        next = randomBlock();
        addToBoard();
        randomRotate(current);
        movementListeners.forEach(GravityListener::onChangedNext);
        move(Direction.DOWN);
        move(Direction.DOWN);

    }

    public void stop() {
        board.stop();
    }
}
