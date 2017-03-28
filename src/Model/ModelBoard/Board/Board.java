package Model.ModelBoard.Board;
import Model.ModelBoard.Direction;
import Model.ModelBoard.Observers.GravityListener;
import Model.ModelBoard.Pieces.GravityDeomon;
import Model.ModelBoard.Pieces.Identificator;
import Model.ModelBoard.Pieces.Piece;
import Model.ModelBoard.Position.Position;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Created by Irindul on 09/02/2017.
 *
 * Class Board contains movement collisiosns check and multithread actions.
 */
public class Board {
    
    private final Map<Position, Piece> collisions;
    private ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
    private Map<Piece, ScheduledFuture<?>> futures = new HashMap<>();
    private Map<Piece, Thread> daemons = new HashMap<>();
    private int height;
    private int width;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock read =  lock.readLock();
    private Lock write =  lock.writeLock();
    private GravityListener listener;
    private ArrayList<Piece> debug = new ArrayList<>();

    public Board(int height, int width) {
        this.height = height;
        this.width = width;
        collisions = new ConcurrentHashMap<>();

    }

    public void addListener(GravityListener listener){
        this.listener = listener;
    }

    public  Board(Board board) {

        collisions = new HashMap<>();
        synchronized (collisions){
            collisions.putAll(board.collisions);
        }
        this.height = board.height;
        this.width = board.width;

    }

    public void addPiece(Piece piece){
        write.lock();
        executor.setRemoveOnCancelPolicy(true);
        piece.getPositions().forEach(
                position -> collisions.put(position, piece)
        );

        debug.add(piece);

        write.unlock();
    }

    public void addDaeomon(Piece piece, GravityListener listener){

        Thread t = new Thread(new GravityDeomon(this, piece, listener));
        t.setDaemon(true);
        daemons.put(piece, t);
        futures.put(piece, executor.scheduleAtFixedRate(t, 0, 30, TimeUnit.MILLISECONDS));
    }


    public boolean checkMovement(Direction direction, Piece piece){

        read.lock();
        if(Thread.currentThread().isInterrupted()) {
            read.unlock();
            return false;
        }
        if(!collisions.containsValue(piece)) {
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

        read.unlock();

        return ok;
    }


    private boolean checkCollide(Position pos, Piece piece) {
        return !(pos.getX() >= 0 && pos.getY() >= 0 && pos.getX() < height && pos.getY() < width)
                || collisions.containsKey(pos) && !collisions.get(pos).equals(piece);

    }

    public void movePiece(Direction direction, Piece piece){
        write.lock();
        if(Thread.currentThread().isInterrupted()){
            write.unlock();
            return;
        }

        try{

            if(collisions.containsValue(piece)){
                
                if(checkMovement(direction, piece)){
                    piece.getPositions().forEach(collisions::remove);
                    piece.move(direction);
                    piece.getPositions().forEach(position -> collisions.put(position, piece));
                }
            } else {
                if (futures.get(piece) != null) {
                    futures.get(piece).cancel(true);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            write.unlock();
        }
    }

    public boolean contains(Piece p){
        return collisions.containsValue(p);
    }


    public boolean isEmptyRow(int i){
        read.lock();
        for (int j = 0; j < width; j++) {
            Position toCheck = new Position(i, j);
            if(collisions.containsKey(toCheck)){
                return false;
            }
        }
        read.unlock();
        return true;
    }

    private boolean isFullRow(int i){
        for (int j = 0; j < width; j++) {
            Position toCheck = new Position(i, j);
            if(!collisions.containsKey(toCheck)){
                return false;
            }
        }
        return true;
    }

    public int sweep(){
        
        write.lock();

        try{
            int count = 0;
            for (int i = height-1; i >= 0; i--) {
                if(isFullRow(i)){
                    count++;
                    for (int j = 0; j < width; j++) {
                        Position toRemove = new Position(i, j);
                        Piece p = collisions.get(toRemove);
                        if(futures.get(p) != null) {
                            futures.remove(p).cancel(true);
                        }
                        if( daemons.get(p) != null) {
                            daemons.remove(p).interrupt();
                        }


                        p.removePosition(toRemove);
                        collisions.remove(toRemove);

                    }
                }
            }
            if(count > 0){
                collisions.values().stream()
                        .filter(Piece::hasBeenChanged)
                        .forEach(piece -> {
                            this.resolveHoles(piece);
                                if(futures.get(piece) != null)
                                futures.remove(piece).cancel(true);
                                if (daemons.get(piece)!= null) {
                                    daemons.remove(piece).interrupt();
                                }

                            listener.update(piece);
                        });
            }

            futures.entrySet().stream()
                    .map(Map.Entry::getKey)
                    .filter(Piece::onlyFalse)
                    .forEach(piece -> {
                        futures.get(piece).cancel(true);
                        executor.remove(daemons.get(piece));
                        collisions.entrySet().removeIf(entry -> entry.getValue().equals(piece));
                        listener.onCleanUp(piece);
                    });

            futures.entrySet()
                    .removeIf(entry -> entry.getKey().onlyFalse());


            collisions.entrySet()
                    .stream()
                    .map(Map.Entry::getValue)
                    .forEach(piece -> {
                        Thread t = new Thread(new GravityDeomon(this, piece, listener));
                        t.setDaemon(true);
                        //daemons.put(piece, t);
                        if(futures.get(piece) == null){
                            futures.put(piece, executor.scheduleAtFixedRate(t, 1000, 30, TimeUnit.MILLISECONDS));
                            if(daemons.get(piece) == null)
                                daemons.put(piece, t);
                            else
                                daemons.replace(piece, t);
                        }

                    });

            write.unlock();
            return  count;
        } catch (Exception e){
            e.printStackTrace();
        }

        return 0;

    }

    public void resolveHoles(Piece p){
        write.lock();
        try{
            p.getPositions().forEach(collisions::remove);
            p.resolveHoles();
            p.getPositions().forEach(pos -> collisions.put(pos, p));
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            write.unlock();
        }


    }


    private boolean checkRotation(Piece piece){
        read.lock();
        List<Position> toCheck = piece.getRotations();
        boolean ok = true;

        for(Position pos : toCheck){
            if(checkCollide(pos, piece)) {
                ok = false;
                break;

            }
        }
        read.unlock();
        return ok;
    }

    public void rotateClockWise(Piece piece){
        write.lock();
        try {
            if(checkRotation(piece)){
                piece.getPositions().forEach(collisions::remove);
                piece.rotateClockWise();
                piece.getPositions().forEach( position -> collisions.put(position, piece));
            }
        } catch (Exception e ){
            e.printStackTrace();
        }
        finally {
            write.unlock();

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


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"height\": \"");
        sb.append(height);
        sb.append("\", \"width\": \"");
        sb.append(width);
        sb.append("\", \"collisions\":[");
        collisions.forEach((key, value) -> {
            sb.append("{");
            sb.append("\"key\":");
            sb.append(key.toString());
            sb.append(", \"piece\":");
            sb.append(value.toString());
            sb.append("}");
            if (collisions.entrySet().iterator().hasNext()) {
                sb.append(",");
            }
        });
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("]");
        sb.append("}");

        return sb.toString().replaceAll("\\s+", "");
    }

    private static Board fromJson(String json) {
        CharacterIterator iterator = new StringCharacterIterator(json);
        //String = height":....
        char c;

        //We remove {"height":
        while(iterator.next() != ':');
        iterator.next();

        StringBuilder sb = new StringBuilder();


        while((c = iterator.next()) != '\"')
            sb.append(c);

        //sb = height value
        int height = Integer.parseInt(sb.toString());

        //Remvoe ,"width":
        while(iterator.next() != ':');
        iterator.next();


        sb = new StringBuilder();
        while((c = iterator.next()) != '\"')
            sb.append(c);

        //sb = width value
        int width = Integer.parseInt(sb.toString());

        //We remove ","collisions":
        while (iterator.next() != '[');
        // iterator.next();

        c = iterator.current();

        int countSquare = 0;

        HashMap<Position, Piece> hash = new HashMap<>();
        while( (countSquare != 0 || c != ']') ){

            if(c == '[')
                countSquare++;

            if(c == ']')
                countSquare--;

            if(c == ']' && countSquare == 0)
                break;


            //Removing {"key":
            while(iterator.next() != ':'){
                //break;
            }
            // iterator.next();


            StringBuilder positionJson = new StringBuilder();
            //We extract the json position
            while(( c =iterator.next()) != '}'){
                positionJson.append(c);
            }
            positionJson.append(c);

            Position position = Position.fromJson(positionJson.toString());

            //Removing ,"piece":
            while(iterator.next() != ':');
            iterator.next();



            //Extracting the json piece
            int countBrackets = 0;
            sb = new StringBuilder();
            c= iterator.current();
            while(countBrackets != 0 || c != '}'){
                if(c == '{')
                    countBrackets++;

                if(c == '}')
                    countBrackets--;

                if(c == '}' &&  countBrackets == 0)
                    break;

                sb.append(c);
                c = iterator.next();
            }
            sb.append(c);
            Piece piece = Piece.fromJson(sb.toString());

            hash.put(position, piece);
            iterator.next();
            c = iterator.next();
        }

        Board board = new Board(height, width);
        board.collisions.putAll(hash);


        return board;
    }

    public static Board fromFile(String filename){
        try {
            BufferedReader br = Files.newBufferedReader(Paths.get(filename));
            String jsonBoard = br.readLine();
            br.close();
            return fromJson(jsonBoard);
        } catch (IOException e){
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public void toFile(String filename){
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(this.toString());
            out.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public Piece getPieceAt(Position pos){
        return collisions.get(pos);
    }

    public List<Piece> getPieces(){
        Identificator identificator = new Identificator();
        return collisions.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(identificator::add)
                .collect(Collectors.toList());
    }

    public void stop() {
        futures.forEach((key, value) -> {
            value.cancel(true);
        });

        executor.shutdown();

    }
}


