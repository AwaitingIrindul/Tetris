package ModelBoard.Board;

import ModelBoard.Direction;
import ModelBoard.Pieces.Block;
import ModelBoard.Pieces.BlockAggregate;
import ModelBoard.Position.Position;
import com.sun.xml.internal.bind.v2.TODO;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Irindul on 09/02/2017.
 */
public class Board {

    private Grid grid;
    private List<BlockAggregate> blockAggregates;



    public Board(int height, int width) {
        grid = new Grid(height, width);
        blockAggregates = new ArrayList<>();
    }

    public void addPiece(BlockAggregate piece){
        blockAggregates.add(piece);
        for(Block block : piece.getBlocks()){
            Position pos = block.getPosition();
            grid.placeOnTile(pos.getX(), pos.getY());
        }
    }


    public boolean checkMovement(Direction direction, int ind){
        boolean possible = false;
        Position pos;
        for (Block block: blockAggregates.get(ind).getBlocks()) {
            for (int i = 0; i < block.getHeight(); i++) {
                for (int j = 0; j < block.getWidth(); j++) {
                    pos = direction.getNewPosition(block.getPosition(i , j));
                    int x = pos.getX();
                    int y = pos.getY();
                    if(grid.isInRange(x, y)){
                        if(grid.isEmpty(x, y)){
                            possible = true;
                        }
                    } else {
                        return false;
                    }
                }
            }


        }

        return possible;
    }

    public void movePiece(Direction direction, int i){

        if(checkMovement(direction, i)){
            BlockAggregate blocks = blockAggregates.get(i);
            Position pos;
            for(Block block : blocks.getBlocks()){
                grid.removeFromTile(block.getPosition().getX(), block.getPosition().getY());
                pos = direction.getNewPosition(block.getPosition());
                block.setPosition(pos);
                grid.placeOnTile(pos.getX(), pos.getY());
            }

        }
    }

    public void rotateClockWise(int i){
        rotatePiece(-Math.PI/2, i);
    }
    private void rotatePiece(Double angle, int i){
        Position current;
        boolean possible = true;

        for(Block block : blockAggregates.get(i).getBlocks()){
            for (int j = 0; j < block.getHeight(); j++) {
                for (int k = 0; k < block.getWidth(); k++) {
                    current = block.getPosition(j, k);
                    int newx = (int) (current.getX() * Math.cos(angle) - current.getY() * Math.sin(angle));
                    int newy = (int) (current.getX() * Math.sin(angle) - current.getY() * Math.cos(angle));

                    // TODO: 11/02/2017 change newx/newy formula
                    /*newX = centerX + (point2x-centerX)*Math.cos(x) - (point2y-centerY)*Math.sin(x);
                    newY = centerY + (point2x-centerX)*Math.sin(x) + (point2y-centerY)*Math.cos(x);*/

                    // TODO: 11/02/2017 add Rotation origin on each piece 
                    if(!grid.isEmpty(newx, newy)){
                        possible = false;
                        break;
                    }
                }
            }
        }


        if(possible){
            for(Block block : blockAggregates.get(i).getBlocks()){
                for (int j = 0; j < block.getHeight(); j++) {
                    for (int k = 0; k < block.getWidth(); k++) {
                        current = block.getPosition(j, k);
                        int newx = (int) (current.getX() * Math.cos(angle) - current.getY() * Math.sin(angle));
                        int newy = (int) (current.getX() * Math.sin(angle) - current.getY() * Math.cos(angle));


                        block.setPosition(new Position(newx, newy));
                    }
                }
            }
        }

    }

    public Grid getGrid(){
        return grid;
    }

    public List<BlockAggregate> getBlockAggregates() {
        return blockAggregates;
    }
}
