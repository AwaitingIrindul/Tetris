package ModelBoard.Board;

import ModelBoard.Direction;
import ModelBoard.Pieces.Block;
import ModelBoard.Pieces.BlockAgregat;
import ModelBoard.Position.Position;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Irindul on 09/02/2017.
 */
public class Board {

    private Grid grid;
    private List<BlockAgregat> blockAgregats;



    public Board(int height, int width) {
        grid = new Grid(height, width);
        blockAgregats = new ArrayList<>();
    }

    public void addPiece(BlockAgregat piece){
        blockAgregats.add(piece);
        for(Block block : piece.getBlocks()){
            Position pos = block.getPosition();
            grid.placeOnTile(pos.getX(), pos.getY());
        }
    }


    public boolean checkMovement(Direction direction, int ind){
        boolean possible = true;
        Position pos;
        for (Block block: blockAgregats.get(ind).getBlocks()) {
            pos = direction.getNewPosition(block.getPosition());
            int i = pos.getX();
            int j = pos.getY();
            if(grid.isInRange(i, j)){
                if(grid.isEmpty(pos.getX(), pos.getY())){
                    possible = true;
                }
            } else {
                possible = false;
            }

        }

        return possible;
    }

    public void movePiece(Direction direction, int i){

        if(checkMovement(direction, i)){
            BlockAgregat blocks = blockAgregats.get(i);
            Position pos;
            for(Block block : blocks.getBlocks()){
                grid.removeFromTile(block.getPosition().getX(), block.getPosition().getY());
                pos = direction.getNewPosition(block.getPosition());
                block.setPosition(pos);
                grid.placeOnTile(pos.getX(), pos.getY());
            }

        }
    }

    public Grid getGrid(){
        return grid;
    }

    public List<BlockAgregat> getBlockAgregats() {
        return blockAgregats;
    }
}
