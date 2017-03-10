package ModelBoard.Pieces;

import ModelBoard.Board.Grid;
import ModelBoard.Direction;
import ModelBoard.Position.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by Irindul on 09/02/2017.
 */
public class BlockAggregate {

    private Position position;
    private Origin origin;
    private int height, width;
    private boolean positions[][];


    public BlockAggregate(int height, int width) {
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

    public void removePosition(Position pos){
        positions[pos.getX() - position.getX()][pos.getY() - position.getY()] = false;
    }

    public BlockAggregate(String[][] scheme, Position startingPosition){
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

    public BlockAggregate(BlockAggregate b){
        this.position = b.position;
        height = b.height;
        width = b.width;
        positions = new boolean[width][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                positions[i][j] = b.positions[i][j];
            }
        }
        this.origin = new Origin(b.origin);
    }

    public boolean checkRotation(Grid g){
        /*for (Block block : blocks){
            if(!block.checkRotation(g, origin.getPositionOfBlock())){
                possible = false;
            }
        }*/

        return true;
    }

    public void rotateClockWise(){
        //blocks.forEach(block -> block.rotateClockWise(origin.getPositionOfBlock()));
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

    public int getMinimumY(){

        List<Position> positions = getPositions();
        return positions.stream().max((o1, o2) -> Integer.compare(o1.getY(), o2.getY())).get().getY();

    }


    public Position getPosition(){
        return position;
    }
}
