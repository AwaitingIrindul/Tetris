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

    public void resolveHoles() {
        boolean isAlone;
        for (int i = 0; i < width; i++) {
            for (int j = height- 1; j >= 0 ; j--) {
                if(positions[j][i]) {

                    Position tmp = new Position(j, i);
                    Position down = Direction.DOWN.getNewPosition(tmp);

                    Position left = Direction.LEFT.getNewPosition(tmp);
                    Position right = Direction.RIGHT.getNewPosition(tmp);

                    boolean leftOk, rightOk, ok;

                    leftOk = left.getX() < 0 || left.getX() >= height || left.getY() < 0 || left.getY() >= width || isEmpty(left);
                    rightOk = right.getX() < 0 || right.getX() >= height || right.getY() < 0 || right.getY() >= width || isEmpty(right);

                    ok = leftOk && rightOk;

                    if (ok) {
                        for (int k = 0; k < height; k++) {
                            if (isEmpty(down)) {
                                positions[tmp.getX()][tmp.getY()] = false;
                                positions[down.getX()][down.getY()] = true;
                                tmp = new Position(down);
                                down = Direction.DOWN.getNewPosition(down);

                            }
                        }
                    }


                }
            }
        }

    }
}
