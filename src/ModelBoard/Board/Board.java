package ModelBoard.Board;

import ModelBoard.Direction;
import ModelBoard.Pieces.Block;
import ModelBoard.Pieces.BlockAggregate;
import ModelBoard.Position.Position;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Irindul on 09/02/2017.
 */
public class Board {

    private Grid grid;
    private List<BlockAggregate> blockAggregates;
    private int height;
    private int width;


    public Board(int height, int width) {
        grid = new Grid(height, width);
        this.height = height;
        this.width = width;
        blockAggregates = new ArrayList<>();
    }

    public void addPiece(BlockAggregate piece){
        blockAggregates.add(piece);
        Position pos;
        for(Block block : piece.getBlocks()){
            for (int i = 0; i < block.getHeight(); i++) {
                for (int j = 0; j < block.getWidth(); j++) {
                    pos = block.getPosition(i, j);
                    grid.placeOnTile(pos.getX(), pos.getY());
                }
            }

        }
    }


    public boolean checkMovement(Direction direction, int ind){
        boolean possible = false;
        Position pos;
        BlockAggregate blocks = blockAggregates.get(ind);
        for (Block block: blocks.getBlocks()) {
            for (int i = 0; i < block.getHeight(); i++) {
                for (int j = 0; j < block.getWidth(); j++) {
                    pos = direction.getNewPosition(block.getPosition(i , j));
                    int x = pos.getX();
                    int y = pos.getY();

                    if(!blocks.isInBlock(pos)){
                        if(grid.isInRange(x, y)){
                            if(grid.isEmpty(x, y)){
                                possible = true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }

        }

        return possible;
    }

    public int getIndex(BlockAggregate b){
        return blockAggregates.indexOf(b);
    }

    public void movePiece(Direction direction, int i){

        if(checkMovement(direction, i)){
            BlockAggregate blocks = blockAggregates.get(i);
            Position pos;


            cleanGrid(blocks);
            blocks.move(direction);
            replaceBlockOnGrid(blocks);
            Position tmp = blocks.getOrigin().getBlock().getPosition(blocks.getOrigin().getPosition().getX(), blocks.getOrigin().getPosition().getY());

        }


    }

    public void cleanGrid(BlockAggregate b){
        for(Block block: b.getBlocks()){
            for (int i = 0; i < block.getHeight(); i++) {
                for (int j = 0; j < block.getWidth(); j++) {
                    Position tmp = block.getPosition(i, j);
                    grid.removeFromTile(tmp.getX(), tmp.getY());
                }
            }
        }
    }

    public void replaceBlockOnGrid(BlockAggregate b){
        for(Block block: b.getBlocks()){
            for (int i = 0; i < block.getHeight(); i++) {
                for (int j = 0; j < block.getWidth(); j++) {
                    Position tmp = block.getPosition(i, j);
                    grid.placeOnTile(tmp.getX(), tmp.getY());
                }
            }
        }
    }



    public List<Integer> rowsToSweep(){
        List<Integer> rows = new ArrayList<>();
        int count;
        for (int i = 0; i < height; i++) {
            count = 0;
            for (int j = 0; j < width; j++) {
                if(!grid.isEmpty(i, j)){
                    count++;
                }
            }

            if(count == width - 1){
                rows.add(i);
            }
        }

        return rows;
    }

    public void rotateClockWise(int i){
        Position current;
        boolean possible = true;
        BlockAggregate ba = blockAggregates.get(i);
        Position origin = ba.getOrigin().getPositionOfBlock();
        for(Block block : ba.getBlocks()){
            for (int j = 0; j < block.getHeight(); j++) {
                for (int k = 0; k < block.getWidth(); k++) {
                    current = block.getPosition(j, k);

                    /*
                        We calculate the new points according to a rotation of (-PI/2)
                        The new coordinates are computed with this formula :
                        newX  = originX + (currentX - originX) * cos(-pi/2) - (currentY - originY) * sin(-pi/2)
                        newY = originY + (currentX - originX) * sin(-pi/2) - (currentY - originY) * cos(-pi/2)

                        As we know sin(-pi/2) = 1 and cos(-pi/2)  = 0
                        So we simplify  and get the formula below
                     */
                    int newx = origin.getX() + (current.getY() - origin.getY());
                    int newy = origin.getY() - current.getX() + origin.getX();

                    if(grid.isInRange(newx, newy)){
                        if(!ba.isInBlock(new Position(newx, newy))) { //If the new coordinates are not available
                            if(!grid.isEmpty(newx, newy)){
                                possible = false; //The rotation isn't possible
                                break;
                            }
                        }

                    } else {
                        possible=false;
                        break;
                    }



                }
            }
        }


        if(possible){
             //If the rotation is possible we recompute every rotation and set it
            cleanGrid(ba);
            for(Block block: blockAggregates.get(i).getBlocks() ){
                for (int j = 0; j < block.getHeight(); j++) {
                    for (int k = 0; k < block.getWidth(); k++) {
                        current = block.getPosition(j, k);
                        int newx = origin.getX() + (current.getY() - origin.getY());
                        int newy = origin.getY() - current.getX() + origin.getX();

                        block.setPosition(j, k, new Position(newx, newy));

                    }
                }
            }

            replaceBlockOnGrid(ba);
        }

    }

    public Grid getGrid(){
        return grid;
    }

    public List<BlockAggregate> getBlockAggregates() {
        return blockAggregates;
    }
}
