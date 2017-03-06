package ModelBoard.Board;

import ModelBoard.Direction;
import ModelBoard.Pieces.BlockAggregate;
import ModelBoard.Position.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by Irindul on 09/02/2017.
 */
public class Board {

    private Grid grid;
    /* private List<BlockAggregate> blockAggregates; */
    private Map<Position, BlockAggregate> collisions;
    private int height;
    private int width;


    public Board(int height, int width) {
        grid = new Grid(height, width);
        this.height = height;
        this.width = width;
        //blockAggregates = new ArrayList<>();
        collisions = new HashMap<>();
    }

    public Board(Board board) {
        this.grid = new Grid(board.grid);
        collisions = new HashMap<>();
        collisions.putAll(board.collisions);

        this.height = board.height;
        this.width = board.width;
    }

    public void addPiece(BlockAggregate piece){
        piece.getPositions().forEach(
                position -> collisions.put(position, piece)
        );

    }


    public boolean checkMovement(Direction direction, BlockAggregate blocks){
        
        
        List<Position> toCheck = blocks.getPositions().stream()
                .map(direction::getNewPosition).collect(Collectors.toList());

        boolean ok = true;

        for(Position pos : toCheck){
            if(checkCollide(pos, blocks)) {
                ok = false;
                break;
                
            }
        }

        return ok;
    }

    private boolean checkCollide(Position pos, BlockAggregate blocks){
        if (pos.getX() >= 0 && pos.getY() >= 0 && pos.getX() < height && pos.getY() < width) {
            return collisions.containsKey(pos) && !collisions.get(pos).equals(blocks);

        } else {
            return true;
        }

       // return false;

    }

    public void movePiece(Direction direction, BlockAggregate blocks){

        if(checkMovement(direction, blocks)){
            blocks.getPositions().forEach( position -> collisions.remove(position));
            blocks.move(direction);
            blocks.getPositions().forEach(position -> collisions.remove(position));
        }


    }


    public boolean isEmptyRow(int i){
        return grid.isEmptyRow(i);

    }

    public boolean isFullRow(int i){
        return grid.isFullRow(i);
    }

    public int sweep(){
        return grid.sweep();
    }

    public BlockAggregate getBlockAggregate(Position p){
        return collisions.get(p);
    }




    public void rotateClockWise(BlockAggregate blocks){
        if(blocks.checkRotation(grid))
            blocks.rotateClockWise();
    }

    public Grid getGrid(){
        return grid;
    }

    public List<BlockAggregate> getBlockAggregates() {
        return null;
    }

    public int rowsToSweep() {
        return grid.rowsToSweep();
    }
}


