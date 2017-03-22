package ModelBoard.Board;
import ModelBoard.Direction;
import ModelBoard.Observers.GravityListener;
import ModelBoard.Pieces.GravityDeomon;
import ModelBoard.Pieces.Piece;
import ModelBoard.Position.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Board {
    
    private ConcurrentMap<Position, Piece> collisions;
    private ScheduledExecutorService executor = (ScheduledExecutorService) Executors.newScheduledThreadPool(8);
    private Map<Piece, ScheduledFuture<?>> futures = new HashMap<>();
    private int height;
    private int width;
    private ReentrantLock lock = new ReentrantLock();
    private GravityListener listener;

    public Board(int height, int width) {
        this.height = height;
        this.width = width;
        collisions = new ConcurrentHashMap<>();
    }

    public void addListener(GravityListener listener){
        this.listener = listener;
    }

    public  Board(Board board) {

        collisions = new ConcurrentHashMap<>();
        synchronized (collisions){
            collisions.putAll(board.collisions);
        }
        this.height = board.height;
        this.width = board.width;

    }

    public void addPiece(Piece piece){
        piece.getPositions().forEach(
                position -> collisions.put(position, piece)
        );
    }

    public void addDaeomon(Piece piece, GravityListener listener){

        Thread t = new Thread(new GravityDeomon(this, piece, listener));
        t.setDaemon(true);

        futures.put(piece, executor.scheduleAtFixedRate(t, 0, 30, TimeUnit.MILLISECONDS));
    }


    public synchronized boolean checkMovement(Direction direction, Piece piece){

        if(!collisions.containsValue(piece)) {
            System.out.println("Piece not in collision anymore : " + Thread.currentThread().getName());
           return false;
        }

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


    private synchronized boolean checkCollide(Position pos, Piece piece){
        if (pos.getX() >= 0 && pos.getY() >= 0 && pos.getX() < height && pos.getY() < width) {
            return collisions.containsKey(pos) && !collisions.get(pos).equals(piece);

        } else {
            return true;
        }

       // return false;

    }

    public synchronized void movePiece(Direction direction, Piece piece){
        lock.lock();
        try{
            if(collisions.containsValue(piece)){
                if(checkMovement(direction, piece)){
                    piece.getPositions().forEach( position -> collisions.remove(position));
                    piece.move(direction);
                    piece.getPositions().forEach(position -> collisions.put(position, piece));
                }
            } else {
                System.out.println("Why am i fucking called");
                piece.getPositions().forEach(pos -> System.out.println("Pos : " + pos.getX() + " "  + pos.getY()));

                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                
            }
        } finally {
            lock.unlock();
        }



    }

    public boolean contains(Piece p){
        return collisions.containsValue(p);
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
        lock.lock();
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
        if(count > 0){
            collisions.values().stream()
                   .filter(Piece::hasBeenChanged)
                   .forEach(this::resolveHoles);
        }

        futures.entrySet().stream()
               .map(Map.Entry::getKey)
               .filter(Piece::onlyFalse)
               .forEach(piece -> {
                   futures.get(piece).cancel(true);
                   collisions.entrySet().removeIf(entry -> entry.getValue().equals(piece));
                   listener.onCleanUp(piece);
               });

        futures.entrySet()
              .removeIf(entry -> entry.getKey().onlyFalse());



        lock.unlock();
        return count;
    }

    public void resolveHoles(Piece p){
        lock.lock();
        try{
            p.getPositions().forEach(pos -> collisions.remove(pos));
            p.resolveHoles();
            p.getPositions().forEach(pos -> collisions.put(pos, p));
        } finally {
                lock.unlock();
        }


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

    public void onQuit(){
        executor.shutdown();
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


