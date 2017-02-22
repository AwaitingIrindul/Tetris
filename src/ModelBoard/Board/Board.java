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
        for (int j = 0; j < width; j++) {
            if(!grid.isEmpty(i, j)){
                return false;
            }
        }

        return true;

    }

    public boolean isFullRow(int i){
        for (int j = 0; j < width; j++) {
            if(grid.isEmpty(i, j)){
                return false;
            }
        }

        return true;
    }

    public int sweep(){
        List<Integer> rows = rowsToSweep();
        Position tmp = new Position(0, 0);
        for(int i : rows){
            for (int j = 0; j < width; j++) {
                if(!grid.isEmpty(i, j)){
                    tmp.setXY(i, j);
                    getBlockAggregate(tmp).remove(getBlockAggregate(tmp).getBlock(tmp));
                    grid.removeFromTile(i, j);
                }
            }
        }

       return rows.size();
    }

    public BlockAggregate getBlockAggregate(Position p){
        for(BlockAggregate b : blockAggregates){
            if(b.isInBlock(p))
                return b;
        }

       throw new NullPointerException();
    }


    public List<Integer> rowsToSweep(){
        List<Integer> rows = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            if(isFullRow(i)){
                rows.add(i);
            }
        }

        return rows;
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
}
