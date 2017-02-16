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



    public Board(int height, int width) {
        grid = new Grid(height, width);
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

    public void rotateClockWise(int i){
        rotatePiece(-Math.PI/2, i);
    }
    private void rotatePiece(Double angle, int i){
        Position current;
        boolean possible = true;
        BlockAggregate ba = blockAggregates.get(i);
        Position origin = ba.getOrigin().getPositionOfBlock();
        System.out.println("Origin : " +origin.getX() + " " + origin.getX());
        for(Block block : ba.getBlocks()){
            for (int j = 0; j < block.getHeight(); j++) {
                for (int k = 0; k < block.getWidth(); k++) {
                    current = block.getPosition(j, k);

                    System.out.println("Current : " + current.getX() + " " + current.getY());


                    int newx = (int)( origin.getX() + (current.getX() - origin.getX())*Math.cos(angle) - (current.getY() - origin.getY())*Math.sin(angle) );
                    int newy = (int)( origin.getY() + (current.getX() - origin.getX())*Math.sin(angle) + (current.getY() - origin.getY())*Math.cos(angle) );
                    /*newX = centerX + (point2x-centerX)*Math.cos(x) - (point2y-centerY)*Math.sin(x);
                    newY = centerY + (point2x-centerX)*Math.sin(x) + (point2y-centerY)*Math.cos(x);*/
                    System.out.println("New : " + newx + " " + newy);

                    if(grid.isInRange(newx, newy)){
                        if(!ba.isInBlock(new Position(newx, newy))) {
                            if(!grid.isEmpty(newx, newy)){
                                possible = false;
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
            System.out.println("Rotate");


            // TODO: 16/02/2017 OPTIMIZE
            cleanGrid(ba);
            for(Block block: blockAggregates.get(i).getBlocks() ){
                for (int j = 0; j < block.getHeight(); j++) {
                    for (int k = 0; k < block.getWidth(); k++) {
                        current = block.getPosition(j, k);
                        int newx = (int)( origin.getX() + (current.getX() - origin.getX())*Math.cos(angle) - (current.getY() - origin.getY())*Math.sin(angle) );
                        int newy = (int)( origin.getY() + (current.getX() - origin.getX())*Math.sin(angle) + (current.getY() - origin.getY())*Math.cos(angle) );

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
