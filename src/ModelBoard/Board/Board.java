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


    public synchronized boolean checkMovement(Direction direction, Piece piece){

        List<Position> toCheck = piece.getPositions().stream()
                .map(direction::getNewPosition).collect(Collectors.toList());
        boolean ok = true;

        for(Position pos : toCheck){
            if(checkCollide(pos, piece)) {
                ok = false;
                break;

            }
        }

        return ok;
    }

    public void isInRange(Position pos){

    }

    private synchronized boolean checkCollide(Position pos, Piece piece){
        if (pos.getX() >= 0 && pos.getY() >= 0 && pos.getX() < height && pos.getY() < width) {
            return collisions.containsKey(pos) && !collisions.get(pos).equals(piece);

        } else {
            return true;
        }

       // return false;

    }

    public synchronized void movePiece(Direction direction, Piece piece){
            if(checkMovement(direction, piece)){
                piece.getPositions().forEach( position -> collisions.remove(position));
                piece.move(direction);
                piece.getPositions().forEach(position -> collisions.put(position, piece));
            }
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
            } else {
                if (count > 0){
                    boolean moved = false;
                    Piece tmp = null;
                    for (int j = 0; j < width; j++) {
                        Position toMove = new Position(i + count, j);
                        Piece piece = collisions.get(toMove);

                        if (piece != null) {
                            piece.getPositions().forEach(pos -> collisions.remove(pos));


                            if (tmp == null) {
                                tmp = piece;
                                movePiece(Direction.DOWN, piece);
                            } else if (piece != tmp) {
                                tmp = piece;
                                movePiece(Direction.DOWN, piece);
                            }

                            piece.resolveHoles();

                            piece.getPositions().forEach(pos -> collisions.put(pos, piece));

                        }


                       // collisions.remove(toMove);
                    }
                }
            }
        }




        
        return count;
    }

    public Piece getBlockAggregate(Position p){
        return collisions.get(p);
    }


    public boolean checkRotation(Piece piece){

        List<Position> toCheck = piece.getRotations();
        boolean ok = true;

        for(Position pos : toCheck){
            if(checkCollide(pos, piece)) {
                ok = false;
                break;

            }
        }
        return ok;
    }

    public void rotateClockWise(Piece piece){

        if(checkRotation(piece)){
            piece.getPositions().forEach( position -> collisions.remove(position));
            piece.rotateClockWise();
            piece.getPositions().forEach( position -> collisions.put(position, piece));
        }

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

    public boolean isInPiece(Piece p, Position pos){
        return collisions.getOrDefault(pos, new Piece(0,0)).equals(p);
    }

    public boolean isEmpty(Position pos) {
        return pos.getX() >= 0 && pos.getY() >= 0 && pos.getX() < height && pos.getY() < width && !collisions.containsKey(pos);
    }
}


