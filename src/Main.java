import ModelBoard.Pieces.BlockAggregate;
import ModelTetris.BlockFactory;
import ModelTetris.Player.Evaluator;
import ModelTetris.Player.GeneticAlgorithm;
import ModelTetris.Tetris;
import ModelTetris.TetrisBlocks;
import View.TetrisGame;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Main {

    public static void main(String[] args) {

        javafx.application.Application.launch(TetrisGame.class);

//        BlockAggregate b = BlockFactory.get(TetrisBlocks.Straight);


    }
}
