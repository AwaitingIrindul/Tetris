package ModelTetris.Player;

import ModelBoard.Direction;
import ModelBoard.Pieces.Block;
import ModelBoard.Pieces.BlockAggregate;
import ModelBoard.Position.Position;
import ModelTetris.Tetris;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irindul on 18/02/2017.
 */
public class ArtificialIntelligence {
    Tetris tetris;

    public ArtificialIntelligence(Tetris tetris) {
        this.tetris = tetris;
    }


    public void executeNextMove(){
        Direction direction = Direction.RIGHT;
        List<Tetris> moves = computeEveryMove();
        System.out.println("Printing all possible moves : ");

        for(Tetris t : moves){
            t.getGrid().display();
            System.out.println();
            System.out.println();


        }
        tetris.move(direction);

    }

    private List<Tetris> computeEveryMove(){
        Tetris startingGrid = new Tetris(tetris); //We copy the game so we don't affect it


        //We place the piece on the left upper corner
        for (int i = 0; i < Tetris.width; i++) {
            startingGrid.move(Direction.LEFT);
        }
        BlockAggregate current = startingGrid.getCurrent();

        //Array list of every tetris game once the block is at a final position
        List<Tetris> possibleMoves = new ArrayList<>();

        int iterations = Tetris.width - current.getMaximumY();
        for (int i = 0; i < iterations; i++) {

            Tetris possible = new Tetris(startingGrid);

            for (int j = 0; j < i; j++) { //We shift one more at every possibility
                possible.move(Direction.RIGHT);
            }

            //We place the piece on the bottom
            for (int j = 0; j < Tetris.height; j++) {
                possible.move(Direction.DOWN);
            }

            possibleMoves.add(possible);
        }

        //TODO add rotation
        //TODO add next

        return possibleMoves;
    }
}
