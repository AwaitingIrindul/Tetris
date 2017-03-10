package ModelBoard.Pieces;

import ModelBoard.Board.Grid;
import ModelBoard.Direction;
import ModelBoard.Position.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Piece implements RemovalListener{

    private Position position;
    private Origin origin;
    private int height, width;
    private boolean positions[][];

    private ArrayList<RemovalListener> listeners;


    public Piece(int height, int width) {
        position = new Position(0, 0);
        this.height = height;
        this.width = width;
        positions = new boolean[height][width];
        listeners = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                positions[i][j] = false;
            }
        }
    }

    public Piece(String[][] scheme, Position startingPosition){
        this(scheme.length, scheme[0].length);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(scheme[i][j].equals("1")){
                    positions[i][j] = true;
                } else {
                    positions[i][j] = false;
                }
            }
        }
        position = startingPosition;

    }

    public Piece(Piece b){
        this.position = b.position;
        height = b.height;
        width = b.width;
        positions = new boolean[width][height];
        listeners = new ArrayList<>();
        listeners.addAll(b.listeners);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                positions[i][j] = b.positions[i][j];
            }
        }
        this.origin = new Origin(b.origin);
    }


    public void removePosition(Position pos){
        positions[pos.getX() - position.getX()][pos.getY() - position.getY()] = false;
        listeners.forEach(RemovalListener::onDown);
    }


    public List<Position> getLowers(){
        List<Position> positions = new ArrayList<>();
        int maxHeight = 0;
        boolean oneLineNotEmpty = false;
        for (int i = height-1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                if(this.positions[i][j]){
                    oneLineNotEmpty = true;
                    positions.add(new Position(position.getX() + i, position.getY() + j));
                }
            }
            if(oneLineNotEmpty)
                break;
        }

        return positions;

    }

    private boolean[][] rotate(){
        int M = positions.length;
        int N = positions[0].length;
        boolean rotated[][] = new boolean[N][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                rotated[j][M-1-i] = positions[i][j];
            }
        }
        return rotated;
    }

    public void rotateClockWise(){

        positions = rotate();
        height = positions.length;
        width = positions[0].length;
    }

    public boolean isInBlock(Position pos){

        return false;
    }
    public void setOrigin(Origin origin){
        this.origin = origin;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void move(Direction d){
        position = d.getNewPosition(position);
    }

    public List<Position> getPositions(){
        List<Position> positionsList = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(positions[i][j])
                    positionsList.add(new Position(position.getX()+i, position.getY()+j));
            }
        }

        return positionsList;
    }

    public List<Position> getRotations(){
        List<Position> rotationList = new ArrayList<>();
        boolean[][] rotated = rotate();
        for (int i = 0; i < rotated.length; i++) {
            for (int j = 0; j < rotated[0].length; j++) {
                if(rotated[i][j])
                    rotationList.add(new Position(position.getX()+i, position.getY()+j));
            }
        }

        return rotationList;
    }

    public int getMinimumY(){

        List<Position> positions = getPositions();
        return positions.stream().min((o1, o2) -> Integer.compare(o1.getY(), o2.getY())).get().getY();

    }


    public Position getPosition(){
        return position;
    }

    public void addListener(RemovalListener l){
        listeners.add(l);
    }

    @Override
    public void onDown() {
        this.move(Direction.DOWN);
        listeners.stream().filter(listener -> listener != this)
                  .forEach(RemovalListener::onDown);
    }
}
