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

    public Board(Board board) {
        this.grid = new Grid(board.grid);
        this.blockAggregates = new ArrayList<>();
        for (int i = 0; i < board.blockAggregates.size(); i++) {
            blockAggregates.add(new BlockAggregate(board.blockAggregates.get(i)));
        }
        this.height = board.height;
        this.width = board.width;
    }

    public boolean addPiece(BlockAggregate piece){
        blockAggregates.add(piece);
        Position pos;
        boolean possible = true;
        for(Block block : piece.getBlocks()){
            for (int i = 0; i < block.getHeight(); i++) {
                for (int j = 0; j < block.getWidth(); j++) {
                    pos = block.getPosition(i, j);
                    if(!grid.isEmpty(pos.getX(), pos.getY())){
                        possible = false;
                    }

                }
            }
        }

        if(possible){
            for(Block block : piece.getBlocks()){
                for (int i = 0; i < block.getHeight(); i++) {
                    for (int j = 0; j < block.getWidth(); j++) {
                        pos = block.getPosition(i, j);
                        grid.placeOnTile(pos.getX(), pos.getY());
                    }
                }
            }
            return true;
        } else {
            return false;
        }

    }


    public boolean checkMovement(Direction direction, BlockAggregate blocks){
        return blocks.checkMovement(direction, grid);
    }

    public void movePiece(Direction direction, BlockAggregate blocks){

        if(checkMovement(direction, blocks)){
            blocks.move(direction);
        }


    }


    public boolean isEmptyRow(int i){
        return grid.isEmptyRow(i);

    }

    public boolean isFullRow(int i){
        return grid.isFullRow(i);
    }

    public int sweep(){
        return grid.sweep();
    }

    public BlockAggregate getBlockAggregate(Position p){
        for(BlockAggregate b : blockAggregates){
            if(b.isInBlock(p))
                return b;
        }

       throw new NullPointerException();
    }




    public void rotateClockWise(BlockAggregate blocks){
        if(blocks.checkRotation(grid))
            blocks.rotateClockWise(grid);
    }

    public Grid getGrid(){
        return grid;
    }

    public List<BlockAggregate> getBlockAggregates() {
        return blockAggregates;
    }

    public int rowsToSweep() {
        return grid.rowsToSweep();
    }
}


