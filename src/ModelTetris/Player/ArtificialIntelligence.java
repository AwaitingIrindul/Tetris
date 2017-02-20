package ModelTetris.Player;

import ModelBoard.Direction;
import ModelBoard.Pieces.BlockAggregate;
import ModelTetris.Tetris;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Irindul on 18/02/2017.
 */
public class ArtificialIntelligence {
    Tetris tetris;
    Evaluator evaluator;
    LinkedList<Direction> directions;
    private boolean hasChanged;

    public ArtificialIntelligence(Tetris tetris) {
        this.tetris = tetris;
        evaluator = new Evaluator(-0.510066, 0.760666, -0.35663, -0.184483);
        directions = new LinkedList<>();
        hasChanged = true;
    }


    public void executeNextMove(){
        if(hasChanged){
            directions.clear();

            hasChanged = false;
            Map<Tetris, Map.Entry<Integer, Integer>> moves = computeEveryMove();
            Map<Tetris, Double> evaluations = new HashMap<>();

            moves.forEach((tetris, movement) -> {
                //tetris.applyGravity();
                evaluations.put(tetris, evaluator.evaluate(tetris));
            });

            Tetris t = evaluations.entrySet().stream() //Hashmap to stream
                    ///We get the maximum with a custom comparator
                    .max((o1, o2) -> o1.getValue() > o2.getValue() ? 1 : -1)
                    //We get the Set that match the maximum value
                    .get()
                    //We return the key
                    .getKey();


            System.out.println("Best score : " + evaluations.get(t));

            for (int i = 0; i < moves.get(t).getKey(); i++) {
                tetris.rotate();
            }

            for (int i = 0; i < Tetris.width; i++) {
                tetris.move(Direction.LEFT);
            }
            directionToGrid(moves.get(t).getValue());

        }


        if(!directions.isEmpty()){
            tetris.move(directions.removeFirst());
        } else {
            tetris.move(Direction.DOWN);
        }


    }

    public void hasChanged(){
        hasChanged = true;
    }

    private void directionToGrid(int numberOfRight){

        for (int i = 0; i < numberOfRight; i++) {
            directions.add(Direction.RIGHT);
        }
    }


    private Map<Tetris, Map.Entry<Integer, Integer>> computeEveryMove(){

        //We place the piece on the left upper corner
        /*for (int i = 0; i < Tetris.width; i++) {
            tetris.move(Direction.LEFT);
        }*/


        Tetris startingGrid = new Tetris(tetris); // We copy the grid so we don't affect it
        BlockAggregate current = startingGrid.getCurrent();

        //HashMap of every tetris game once the block is at a final position
        Map<Tetris, Map.Entry<Integer, Integer>> possibleMoves = new HashMap<>();
        //THe integer will store the number of RIGHT movement needed to achieve the posssible grid position;


        for (int k = 0; k < 4; k++) {
            Tetris rotation = new Tetris(startingGrid);
            for (int i = 0; i < k; i++) {
                rotation.rotate();
            }

            for (int i = 0; i < Tetris.width; i++) {
                rotation.move(Direction.LEFT);
            }

            int iterations = Tetris.width - current.getMaximumY();
            for (int i = 0; i < iterations; i++) {

                Tetris possible = new Tetris(rotation);

                for (int j = 0; j < i; j++) { //We shift one more at every possibility
                    possible.move(Direction.RIGHT);
                }

                //We place the piece on the bottom
                for (int j = 0; j < Tetris.height; j++) {
                    possible.move(Direction.DOWN);
                }

                possibleMoves.put(possible, new AbstractMap.SimpleEntry<>(k, i));
            }
        }


        //TODO add next

        return possibleMoves;
    }
}
