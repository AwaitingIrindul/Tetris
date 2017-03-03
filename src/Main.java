import ModelTetris.Player.Evaluator;
import ModelTetris.Player.GeneticAlgorithm;
import ModelTetris.Tetris;
import View.TetrisGame;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Main {

    public static void main(String[] args) {

        javafx.application.Application.launch(TetrisGame.class);

        /*GeneticAlgorithm ga = new GeneticAlgorithm();
        
        Evaluator e = ga.train(2000);
        e.display();*/


    }
}
