package ModelBoard.Pieces;

import ModelBoard.Board.Board;
import ModelBoard.Board.Grid;
import ModelBoard.Direction;
import ModelBoard.Position.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Piece{

    private Position position;
    private int height, width;
    private boolean positions[][];
    private boolean hasBeenChanged = false;


    public Piece(int height, int width) {
        position = new Position(0, 0);
        this.height = height;
        this.width = width;
        positions = new boolean[height][width];
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
        positions = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                positions[i][j] = b.positions[i][j];
            }
        }
    }


    public void removePosition(Position pos){
        positions[pos.getX() - position.getX()][pos.getY() - position.getY()] = false;
        if(!onlyFalse())
            setHasChanged(true);
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

    public boolean isInBlock(Position absolutePos) {

        int i = absolutePos.getX() - position.getX();
        int j = absolutePos.getY() - position.getY();

        return i >= 0 && i < height && j >= 0 && j < width && positions[i][j];
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

    private boolean isEmpty(Position pos){
        int i = pos.getX();
        int j = pos.getY();
        return i >= 0 && i < height && j >= 0 && j < width && !positions[i][j];
    }
    
    private boolean isEmptyRow(int r){
        for (int c = 0; c < width; c++) {
            if(positions[r][c])
                return false;
        }
        return true;
    }

    
    public void resolveHoles() {
        setHasChanged(false);
        for (int i = 0; i < height; i++) {
            applyGravity();
        }
    }

    private void applyGravity(){
        for (int r = height-2; r >= 0; r--) {
            for (int c = 0; c < width; c++) {
                if(positions[r][c] && !positions[r+1][c]){
                    positions[r][c] = false;
                    positions[r+1][c] = true;
                }
            }
        }
    }

    private void applyGravity(int r) {
        for (int c = 0; c < width; c++) {
            if(positions[r][c] && !positions[r+1][c])
            {
                positions[r][c] = false;
                positions[r+1][c] = true;
            }
        }
    }

    public boolean onlyFalse(){
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(positions[i][j])
                    return false;
            }
        }
        return true;
    }

    public boolean hasBeenChanged(){
        return hasBeenChanged;
    }

    private void setHasChanged(boolean changed){
        hasBeenChanged = changed;
    }
}
