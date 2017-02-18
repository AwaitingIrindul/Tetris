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

        tetris.move(direction);

    }

    private List<Tetris> computeEveryMove(){
        Tetris startingGrid = new Tetris(tetris);


        //We place the piece on the left upper corner
        for (int i = 0; i < Tetris.width; i++) {
            startingGrid.move(Direction.LEFT);
        }
        //To store the initial position of the current block
        List<Position> initialPositions = new ArrayList<>();

        BlockAggregate current = startingGrid.getCurrent();

        //Array list of every tetris game once the block is at a final position
        List<Tetris> possibleMoves = new ArrayList<>();

        //We initialize the position with the new position of the block (upper left corner)
        for(Block b : current.getBlocks()){
            for (int i = 0; i < b.getHeight(); i++) {
                for (int j = 0; j < b.getWidth(); j++) {
                    initialPositions.add(b.getPosition(i, j));
                }
            }
        }

        //We will modify the position so we store them
        List<Position> modifiedPosition = new ArrayList<>();
        int count; //This counter is the position of the block's position in the array
        for (int i = 0; i < (Tetris.width - current.getMaximumY()); i++) {
            Tetris possible = new Tetris(tetris);
            current = possible.getCurrent();
            for(Position pos : initialPositions){
                modifiedPosition.add(new Position(pos.getX(), pos.getY() + i));
            }
            count = 0;
            for(Block b : current.getBlocks()){
                for (int j = 0; j < b.getHeight(); j++) {
                    for (int k = 0; k < b.getWidth(); k++) {
                        //We set the new position
                        b.setPosition(j, k, modifiedPosition.get(count++));
                    }
                }
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
