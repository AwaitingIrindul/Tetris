package ModelBoard.Board;

import ModelBoard.Direction;
import ModelBoard.Pieces.Piece;
import ModelBoard.Position.Position;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * Created by Irindul on 09/02/2017.
 */
public class Board {

    /* private List<BlockAggregate> blockAggregates; */
    private ConcurrentHashMap<Position, Piece> collisions;
    private int height;
    private int width;


    public Board(int height, int width) {
        this.height = height;
        this.width = width;
        //blockAggregates = new ArrayList<>();
        collisions = new ConcurrentHashMap<>();
    }

    public Board(Board board) {
        collisions = new ConcurrentHashMap<>();
        collisions.putAll(board.collisions);

        this.height = board.height;
        this.width = board.width;
    }

    public void addPiece(Piece piece){
        piece.getPositions().forEach(
                position -> collisions.put(position, piece)
        );

    }


    public synchronized boolean checkMovement(Direction direction, Piece blocks){
        
        
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

    private synchronized boolean checkCollide(Position pos, Piece blocks){
        if (pos.getX() >= 0 && pos.getY() >= 0 && pos.getX() < height && pos.getY() < width) {

            return collisions.containsKey(pos) && !collisions.get(pos).equals(blocks);

        } else {
            return true;
        }

       // return false;

    }

    public synchronized void movePiece(Direction direction, Piece blocks){
            if(checkMovement(direction, blocks)){
                blocks.getPositions().forEach( position -> collisions.remove(position));
                blocks.move(direction);
                blocks.getPositions().forEach(position -> collisions.put(position, blocks));
            }
    }

    public void linkPiece(Piece piece){
        piece.getLowers().forEach(pos -> {
            Piece downer = collisions.get(pos);
            if(downer != null){
                downer.addListener(piece);
            }

        });

    }


    public boolean isEmptyRow(int i){
        for (int j = 0; j < width; j++) {
            Position toCheck = new Position(i, j);
            if(collisions.containsKey(toCheck)){
                return false;
            }
        }

        return true;
    }

    public boolean isFullRow(int i){
        for (int j = 0; j < width; j++) {
            Position toCheck = new Position(i, j);
            if(!collisions.containsKey(toCheck)){
                
                return false;
            }
        }
        return true;
    }

    public int sweep(){
        int count = 0;
        for (int i = height-1; i >= 0; i--) {
            if(isFullRow(i)){
                count++;
                for (int j = 0; j < width; j++) {
                    Position toRemove = new Position(i, j);
                    collisions.get(toRemove).removePosition(toRemove);
                    collisions.remove(toRemove);
                }
            }
        }


        
        return count;
    }

    public Piece getBlockAggregate(Position p){
        return collisions.get(p);
    }




    public void rotateClockWise(Piece blocks){

        //// TODO: 06/03/2017 Rotation will change block shape
        //if(blocks.checkRotation(grid))
            blocks.rotateClockWise();
    }

    public List<Piece> getBlockAggregates() {
        return null;
    }

    public int rowsToSweep() {
        int rows = 0;
        for (int i = 0; i < height; i++) {
            if(isFullRow(i)){
                rows++;
            }
        }

        return rows;
    }
}


