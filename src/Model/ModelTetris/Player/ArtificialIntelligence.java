package Model.ModelTetris.Player;

import Model.ModelBoard.Direction;
import Model.ModelBoard.Observers.GravityListener;
import Model.ModelBoard.Pieces.Piece;
import Model.ModelTetris.Tetris;

/**
 * Created by Irindul on 18/02/2017.
 * Contains the artificial intelligence for the Tetris game
 */
public class ArtificialIntelligence implements GravityListener, Runnable{
    private Tetris tetris;
    private Evaluator evaluator;
    private int score;
    private boolean hasChanged;
    private boolean go = true;

    public ArtificialIntelligence(Tetris tetris, Evaluator evaluator) {
        this.tetris = tetris;
        tetris.addGravityListener(this);
        this.evaluator = evaluator;
        score = 0;
        hasChanged = true;
    }


    public void executeNextMove(){


        if(hasChanged){
            hasChanged = false;
            Pair<Pair<Integer, Integer>, Double> moves = computeEveryMove();

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

    public void setHasChanged() {
        this.hasChanged = true;
    }


    private Pair<Pair<Integer, Integer>, Double> computeEveryMove(){
        
        Tetris startingGrid = new Tetris(tetris); // We copy the grid so we don't affect it

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

    int getScore() {
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

    void reset(){
        tetris = new Tetris();
        tetris.addGravityListener(this);
    }

    Evaluator getEvaluator() {
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
    public void onCleanUp(Piece p) {

    }
}
