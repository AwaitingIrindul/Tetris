package Model.ModelTetris.Player;

import Model.ModelBoard.Direction;
import Model.ModelBoard.Observers.GravityListener;
import Model.ModelBoard.Pieces.Piece;
import Model.ModelTetris.Tetris;

import java.util.LinkedList;

/**
 * Created by Irindul on 18/02/2017.
 */
public class ArtificialIntelligence implements GravityListener, Runnable{
    private Tetris tetris;
    private Evaluator evaluator;
    private LinkedList<Direction> directions;
    private int score;
    private static int count = 0;
    private boolean hasChanged;
    private boolean go = true;

    public ArtificialIntelligence(Tetris tetris, Evaluator evaluator) {
        this.tetris = tetris;
        tetris.addGravityListener(this);
        this.evaluator = evaluator;
        directions = new LinkedList<>();
        score = 0;
        hasChanged = true;
    }


    public void executeNextMove(){


        if(hasChanged){
            hasChanged = false;
            Pair<Pair<Integer, Integer>, Double> moves = computeEveryMove(0);

            int rotation = moves.getFirst().getFirst();

            for (int i = 0; i < rotation; i++) {
                tetris.rotate();
            }

            for (int i = 0; i < Tetris.width; i++) {
                tetris.move(Direction.LEFT);
            }

            int rights = moves.getFirst().getSecond();
            for (int i = 0; i < rights; i++) {
                tetris.move(Direction.RIGHT);
            }
        }

       // tetris.move(Direction.DOWN);

    }

    public void setHasChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }


    private Pair<Pair<Integer, Integer>, Double> computeEveryMove(int depth){
        
        Tetris startingGrid = new Tetris(tetris); // We copy the grid so we don't affect it
        Piece current = startingGrid.getCurrent();

        Pair<Pair<Integer, Integer>, Double> best = new Pair<>();

        //THe first integer will sotre the number of rotation needed to be on the position.
        //The second integer will store the number of RIGHT movement needed to achieve the posssible grid position;

        double bestScore = 0;
        for (int k = 0; k < 4; k++) {
            Tetris rotation = new Tetris(startingGrid);
            for (int i = 0; i < k; i++) {
                rotation.rotate();
            }

            for (int i = 0; i < Tetris.width; i++) {
                rotation.move(Direction.LEFT);
            }

            int iterations = Tetris.width; //- current.getMaximumY();
            for (int i = 0; i < iterations; i++) {

                Tetris possible = new Tetris(rotation);

                for (int j = 0; j < i; j++) { //We shift one more at every possibility
                    possible.move(Direction.RIGHT);
                }

                //We place the piece on the bottom
                for (int j = 0; j < Tetris.height; j++) {
                    possible.move(Direction.DOWN);
                }

               // possible.addToBoard();
                double score;

                score = evaluator.evaluate(possible);


                if(score > bestScore || bestScore == 0){
                    bestScore = score;
                    best.setSecond(score);
                    Pair<Integer, Integer> move = new Pair<>();
                    move.setFirst(k);
                    move.setSecond(i);
                    best.setFirst(move);

                }

            }
        }

        return best;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void run() {

        while(go && tetris.getScore() < 2001 && !Thread.currentThread().isInterrupted()){
            
            executeNextMove();
            tetris.applyGravity();

        }
        score = tetris.getScore();
    }

    public void reset(){
        tetris = new Tetris();
        tetris.addGravityListener(this);
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    @Override
    public void onMovement() {

    }


    @Override
    public void onChangedNext() {
        hasChanged = true;
    }

    @Override
    public void onSweep() {

    }


    @Override
    public void onQuit() {
        go = false;
    }

    @Override
    public void onMovement(Piece p) {

    }

    @Override
    public void onCleanUp(Piece p) {

    }
}
